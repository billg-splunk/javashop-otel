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

# First we Implement Opentelemetry Auto-Instrumentation via Dockerfiles for Java ( this is the same implementation for Kubernetes )

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
if need sudo sudo -E docker-compose up -d --build 


# Traces will take a couple minutes ....

# View Service Map

If your instrumentation was successful, the service-map will show latency from the shop service to the products service.

TODO: Service map image
NOTE: Follow the trace, what do we know ? 

XX was called, so we send that trace to a developer they will have to debug the function. 
Since they do not have parameter information it can be a long process.

See the Trace, function is ProductResource.ProductResource

Developer:  must Look through all the CODE in FunctionX to find the problem, AKA Find Needle in Haystack.



#TODO SCREENSHOT Show the Function in File 

Now find the needle in Haystack !

vi products/src/main/java/com/shabushabu/javashop/products/resources/ProductResource.java

/getAllProducts

scroll way .... down 

#Ok enough fun ..let's make this easier

# Manual Instrumentation

To take a deeper look at this issue, we will implement manual instrumentation

Normally, to speed up manual instrumentation in Java you would leverage OpenTelemetry Annotations, which automatically 
create a span for a function. In addition, OpenTelemetry Annotations can be used to generate span tags with parameter 
values for the function in question. 

Additional information regarding OpenTelemetry Annotations can be found here: [OpenTelemetry Annotations](https://opentelemetry.io/docs/instrumentation/java/automatic/annotations/ )

#This tool helps eliminate developer time needed for manual instrumentation 

To expedite manual instrumentation implementation for this exercise, we have provided a tool which will annotate the entire "shop" service with OpenTelemetry 
standard annotations for every method call without having to write any code. 

#This Full-fidelity, Every-method approach is the monolith Use Case with Splunk APM for Java.  

# Run the Annotator to Auto Implement the Annotations. 

cd annotator

mvn exec:java

#Rebuild and Deploy Application

cd ..

mvn clean install

docker-compose up -d --build 
if need sudo sudo -E docker-compose up -d --build 

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
if need sudo sudo -E docker-compose down
   
mvn clean install

docker-compose up -d --build 
if need sudo sudo -E docker-compose up -d --build 

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
if need sudo sudo -E docker-compose up -d --build 

#Now that we have rebuilt and deployed our application, traffic is being sent once again.  

We are waiting a few minutes . . .

Wait for it ... 

#If you do not see Red in your service map, you have successfully completed the Splunk Apm Instrumentation Shop !!

# Have a lovely day.

# End Workshop

