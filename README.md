# Tested on Macos

# You will  need:
Git, Maven, Docker 

# Set Environment Variables

Environment Variables:

export USERNAME=<Your-UserName>

export SPLUNK_ACCESS_TOKEN=<Your-Token>

export SPLUNK_REALM=<Your-Realm>


#  Installation
git clone -b workshop https://github.com/shabuhabs/javashop-otel.git
	
cd javashop-otel

#Implement Opentelemetry Auto-Instrumentation via Dockerfiles for Java 

cd shop

vi Dockerfile

add Otel Java Agent to Java ENTRYPOINT,  ( java -javaagent:splunk-otel-javaagent-all.jar )
See examples at ../Dockerfiles_Instrumented

repeat for:
	instruments / products / stock  
	
#Build Application

cd "javashop-otel directory"

mvn clean install

# Run Application

docker-compose up -d --build 


# Traces will take a couple minutes ....

# View Service Map

If your instrumentation was successful, the service-map will show latency from the shop service to the products service.

TODO: Service map image

Click on shoptester service

click Traces ( right side ) 

Sort by Duration

Select the longest duration trace

TODO: Long Trace Image

Now we can see the long latency occurred in the products service and if we click on products: /products
we can see the offending method was products:ProductResource.getAllProducts

TODO: Show Tags method

Our next step here would be to send that trace to a developer by cliking download trace and
they will have to debug the method. 

Before we do that please take note of the Tags available for the developer to leverage to find root cause.

Note: Since they do not have full parameter information it can be a long process.

TODO: Download Trace Button Image

#Now let's play the role of the developer

As a developer we must debug the function products:ProductResource.getAllProducts to find the problem.

TODO SCREENSHOT Show the Function in File 

Now find the needle in Haystack !

vi products/src/main/java/com/shabushabu/javashop/products/resources/ProductResource.java

/getAllProducts

scroll way .... down 

TODO SCREENSHOT Show the Function in File 

Exit vi
:q!

#Ok enough fun ..let's make this easier for our developer

# Manual Instrumentation

To take a deeper look at this issue, we will implement manual instrumentation

Normally, to speed up manual instrumentation in Java you would leverage [OpenTelemetry Annotations](https://opentelemetry.io/docs/instrumentation/java/automatic/annotations/ )
, which automatically create a span around a method without modifying the actual code inside the method. 

To add even more information to help our developers find the root cause faster,
[OpenTelemetry Annotations](https://opentelemetry.io/docs/instrumentation/java/automatic/annotations/ ) can be used to generate span tags with parameter values for the method in question. 

It is important to remember that any developer should be able to debug a method with knowledge of parameter values at the time of an issue ( exception or latency ). 

To expedite manual instrumentation implementation for this exercise, we have provided a tool which will annotate the entire "shop" service with OpenTelemetry standard annotations for every method call without having to write any code. 

#This Full-fidelity, Every-method approach is the Monolith Use Case with Splunk APM for Java.  


cd annotator

Run the annotator application

mvn exec:java

#Rebuild and Deploy Application


cd ..

mvn clean install

docker-compose down

docker-compose up -d --build 


#Now that we have rebuilt and deployed our application, traffic is being sent once again.

Go back to the Splunk Observability UI and let's see if these annotations help us narrow down the root cause more quickly.

Click back on the trace window

Select the Shop Service under "Services"

TODO: shop Traces Image

Give the traces a couple minutes to generate . . .  


# Woah!!!! We have a New Problem, we are getting a new Exception ! 

TODO: Traces with New Exception Screenshot

Note: we haven't changed the code at all by adding annotations.

Select "Errors Only" 

TODO: Errors Only Screenshot 

Click on a trace with an error present 

TODO: Invalid Locale Exception Screen !!

We can see our Exception is InvalidLocaleException !

The real problem must be related to the new data associated with SRI LANKA as the Exception says
"Non English Characters found in Instrument Data. 

This exception had not surfaced in previous traces because the method where it was thrown 
was NOT covered with Auto Instrumentation.

Once we completed the Manual Instrumentation via the Annotations we added, 
this method was instrumented cover this function and we can now see we had a buried Exception being thrown.


#Let's play Developer once again and fix our issue !

We already know exactly what file to look in and what method to look at as it is called out in the trace.

TODO: Add code.function and code.namespace screenshot

Edit the file	
vi shop/src/main/java/com/shabushabu/javashop/shop/model/Instrument.java

Search for the method buildForLocale

/buildForLocale

Look at Code, notice the Annotation @WithSpan? @WithSpan is an [OpenTelemetry Annotation](https://opentelemetry.io/docs/instrumentation/java/automatic/annotations/ ) for Java that automatically generates a span around a the function that follows.

TODO: Add screen of @WithSpan  

Now let's fix this code. We are going to simply comment this out for now and see if it fixes our latency issue.

type i for insert

Change this : 

if (!isEnglish(title)) {

  throw new InvalidLocaleException("Non English Characters found in Instrument Data");
  
 } else {
 
 System.out.println("Characters OK ");
 
}

To this:

//if (!isEnglish(title)) {

//  throw new InvalidLocaleException("Non English Characters found in Instrument Data");

// } else {

//	System.out.println("Characters OK ");

//}

Make sure you saved your changes to shop/src/main/java/com/shabushabu/javashop/shop/model/Instrument.java

Save Changes in vi

:wq

#Rebuild and Deploy Application

docker-compose down

   
mvn clean install

docker-compose up -d --build 


Waiting a few minutes.....

# Latency Root Cause

TODO: Show map, latency is still there.

Open Service Map in Splunk Observability UI

We can see we still have our original Latency issue, however our exception for Invalid Locale should be gone.

Let's check to see our InvalidLocale Exception is gone.

Click Shop Service

Click Traces on the right


We did remove the exception however it seems removing the Exception did not fix the latency...


Lets Look at the high latency traces causing this spike once again.

Also, lets see if these annotations provide us more relevant information for the next responder once we find the root cause.

NOTE: add additional information Parameter Values at Time of Latency.

Developer can debug very quickly. 

#Now we have the parameter "location=colorado" must be the culprit

vi products/src/main/java/com/shabushabu/javashop/products/resources/ProductResource.java

search for Colorado
/Colorado

We found and fixed the Needle In Haystack more quickly !!! 


if (location.equalsIgnoreCase("Colorado")) {

   // Generate a SLOW sleep of Random time !
   Random random = new Random();
    	  int sleepy = random.nextInt(5000 - 3000) + 3000;
    	  
   try{
    		  Thread.sleep(sleepy);
    		} catch (Exception e){
    		
   }
}
    		
comment out 

/* if (location.equalsIgnoreCase("Colorado")) {

   // Generate a SLOW sleep of Random time !
   Random random = new Random();
    	  int sleepy = random.nextInt(5000 - 3000) + 3000;
    	  
   try{
    		  Thread.sleep(sleepy);
    		} catch (Exception e){
    		
   }
} */
    
save changes to products/src/main/java/com/shabushabu/javashop/products/resources/ProductResource.java

save changes in vi]

:wq
    
#Rebuild and Deploy Application

mvn clean install

docker-compose up -d --build 


#Now that we have rebuilt and deployed our application, traffic is being sent once again.  

We are waiting a few minutes . . .

Wait for it ... 

#If you do not see Red in your service map, you have successfully completed the Splunk Apm Instrumentation Shop !!

# Have a lovely day.

# End Workshop

