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

mvn exec:java

#Rebuild and Deploy Application

cd ..

mvn clean install

docker-compose up -d --build 


#Now that we have rebuilt and deployed our application, traffic is being sent once again.

We are waiting a bit for traces to return to UI...

Go to UI and let's see if these annotations help us narrow down the root cause more quickly.

# Woah!!!! We have a New Problem, we are getting a new Exception ! 

Note: we haven't changed the code at all by adding annotations.

GO To UI, Look at Exception in Trace

InvalidLocaleException !

The real problem must be related to the new data associated with SRI LANKA as the Exception is BadCharacters. 

The exception had not surfaced in previous traces because the function where it was thrown 
was NOT covered with Auto Instrumentation, the Annotations we added, 
however did cover this function and we can see a span in UI with this name:

TODO: SCREEN OF Span in UI !!

Let's Fix it

TODO: Redo this  WORKFLOW with screenshots
Find where in Code InvalidLocaleException is Captured.

Optional: search code for InvalidLocaleException

#Edit the file	
vi shop/src/main/java/com/shabushabu/javashop/shop/model/Instrument.java

Look at Code, notice @WithSpan?

TODO: Add screen of @WithSpan Over 

@WithSpan is an Opentelemetry Annotation for Java that automatically
generates a span around a the function that follows, in this case it is 

# We have to find what is calling this method
#  TODO Get exact function name XXXXXXXXXXXXX

// For now, we are going to comment this out to see if it fixes our latency problem..
// User comments this out, then rebuild and run. 	 	
	//   if (!isEnglish(title)) {
	//    	throw new InvalidLocaleException("Non English Characters found in Instrument Data");
	//    } 
	
Make sure you saved your changes to shop/src/main/java/com/shabushabu/javashop/shop/model/Instrument.java

docker-compose down

   
mvn clean install

docker-compose up -d --build 


Waiting a few minutes.....

# Latency Root Cause

#TODO:Show map, should see execption gone and latency is still there.

Open Service Map

We can see we still have our original Latency issue, however our exception for Invalid Locale is gone. 

It seems removing the Exception did not fix the latency...


Lets Look at the high latency traces causing this spike.

Click on line from SHop to Products ( follow the red numbers ) - 

# Additionally, let's also see if these annotations provide us more relevant information for the next responder once we find the root cause.

NOTE: add additional information Parameter Values at Time of Latency.

Developer can debug very quickly. 

#Now we have the parameter "location=colorado" must be the culprit

vi products/src/main/java/com/shabushabu/javashop/products/resources/ProductResource.java

/Colorado

Find Needle In Haystack more quickly.

	if (location.equals("Colorado"))
    		myCoolFunction_Colorado();
    		
comment out 

	//if (location.equals("Colorado"))
    //		myCoolFunction_Colorado();
    
save changes to products/src/main/java/com/shabushabu/javashop/products/resources/ProductResource.java
    
#Rebuild and Deploy Application

mvn clean install

docker-compose up -d --build 


#Now that we have rebuilt and deployed our application, traffic is being sent once again.  

We are waiting a few minutes . . .

Wait for it ... 

#If you do not see Red in your service map, you have successfully completed the Splunk Apm Instrumentation Shop !!

# Have a lovely day.

# End Workshop

