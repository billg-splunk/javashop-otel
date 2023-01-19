
# Scenario

Here at Buttercup Instruments, our business is expanding !  We have recently added 3 new locations, two locations in the USA, Colorado and Chicago and one international location in SRI Lanka ! 

Our technical staff has already on-boarded the data from these new locations and incorporated them into our inventory application and it is our job to review these improvements and send any issues back to our developers for repairs.

We now have a total of 6 locations !

# Expected Duration ~ 35 minutes


# Set Environment Variables

Environment Variables:

export USERNAME=<Your-UserName>

export SPLUNK_ACCESS_TOKEN=<Your-Token>

export SPLUNK_REALM=<Your-Realm>


# Implement Opentelemetry Auto-Instrumentation via Dockerfiles for Java 

cd shop

vi Dockerfile

add Otel Java Agent to Java ENTRYPOINT,  ( java -javaagent:splunk-otel-javaagent-all.jar )

See examples at:
 
vi ../Dockerfiles_Instrumented

repeat for:
	instruments / products / stock  
	
# Build and Deploy Application

cd "javashop-otel directory"

./BuildAndDeploy.sh

# Users and Workflows
	
As we go through this workshop we will be switching roles from SRE to Developer.  First we will start with first responders or SREs who will identify an issue in Splunk Observability UI.  Next, we will jump to a Developer Role to see how a Developer will solve a problem using trace data identified by our SRE.
	
Of course, we are not requiring 2 people for this workshop as each participant will play both roles.

# View Service Map

If your instrumentation was successful, the service-map will show latency from the shop service to the products service.

![Screen Shot 2022-12-08 at 11 48 58 AM](https://user-images.githubusercontent.com/32849847/206541846-7f0e6462-7659-44bc-bc48-3621c2872fc4.png)


Click on shop service

click Traces ( right side ) 

Sort by Duration

Select the longest duration trace

![LongTrace](https://user-images.githubusercontent.com/32849847/204347798-4f232b7f-7a7a-483f-9d61-f0b535e9ecf0.png)


Now we can see the long latency occurred in the products service and if we click on products: /products
we can see the offending method was products:ProductResource.getAllProducts

![long-trace-detail-GetAllProducts](https://user-images.githubusercontent.com/32849847/204347855-724545bf-c3df-478a-a27d-e4f85f708e15.png)


Our next step here would be to send that trace to a developer by clicking download trace and
they will have to debug the method. 

Before we do that please take note of the Tags available for the developer to leverage to find root cause.

Note: Since they do not have full parameter information it can be a long process.

# Now let's play the role of the developer

As a developer we must debug the function products:ProductResource.getAllProducts to find the problem.


Now find the needle in Haystack !

vi products/src/main/java/com/shabushabu/javashop/products/resources/ProductResource.java

/getAllProducts

scroll way .... down 

![getAllProducts-Haystack](https://user-images.githubusercontent.com/32849847/204348020-eab66183-f3a2-488b-bfba-48a660e9acaa.png)

Exit vi
:q!

OK, enough fun ..let's make this easier for our developer

# Custom Attribution

To take a deeper look at this issue, we will implement Custom Attributes via Opentelemetry Manual Instrumentation

To speed up manual instrumentation in Java you can leverage [OpenTelemetry Annotations](https://opentelemetry.io/docs/instrumentation/java/automatic/annotations/ )
, which automatically create a span around a method without modifying the actual code inside the method. This can be very valueable if you are working with an SRE that may have limited accesss to source code changes.

To add even more information to help our developers find the root cause faster,
[OpenTelemetry Annotations](https://opentelemetry.io/docs/instrumentation/java/automatic/annotations/ ) can be used to generate span tags with parameter values for the method in question. 

It is important to remember that any developer should be able to debug a method with knowledge of parameter values at the time of an issue ( exception or latency ). 

To expedite manual instrumentation implementation for this exercise, we have provided a tool which will annotate the entirety of the "shop" and "products" services with OpenTelemetry standard annotations for every method call without having to write any code. This "annotator" tool will also tag every parameter in every function, which adds a span tag with Parameter=Value.

# This Full-fidelity, Every-method approach is the Monolith Use Case with Splunk APM for Java.  

# Run Manual Instrumentation Tool

cd javashop-otel directory
	
./AutomateManualInstrumentation.sh

#Rebuild and Deploy Application

./BuildAndDeploy.sh


#Now that we have rebuilt and deployed our application, traffic is being sent once again.

Go back to the Splunk Observability UI and let's see if these annotations help us narrow down the root cause more quickly.

Click back on the trace window
	
	
![Screen Shot 2022-11-28 at 7 29 43 AM](https://user-images.githubusercontent.com/32849847/204348366-38b8c82a-02ca-472b-b1aa-feeb746ec1d7.png)


Give the traces a couple minutes to generate . . .  


# Woah!!!! We have a New Problem, we are getting a new Exception ! 

Once traces populate, Select "Errors Only" 
	
![Screen Shot 2022-11-28 at 7 36 50 AM](https://user-images.githubusercontent.com/32849847/204348492-84a4ad45-e11c-4e75-a6a9-d6e52e0eb13e.png)

Note: we haven't changed the code at all by adding annotations.

Click on a trace with an error present 

![Screen Shot 2022-11-28 at 7 38 33 AM](https://user-images.githubusercontent.com/32849847/204348687-12241153-b297-4bd7-9ea8-4b410369e82c.png)


We can see our Exception is InvalidLocaleException !

The real problem must be related to the new data associated with SRI LANKA as the Exception says
"Non English Characters found in Instrument Data. 

This exception had not surfaced in previous traces because the method where it was thrown 
was NOT covered with Auto Instrumentation. 

Once we completed the Manual Instrumentation via the Annotations we added, 
this method was instrumented  and we can now see we had a buried Exception being thrown.

# Let's play Developer once again and fix our issue !

We already know exactly what file to look in and what method to look at as it is called out in the trace.

![Screen Shot 2022-11-28 at 7 43 58 AM](https://user-images.githubusercontent.com/32849847/204349038-3b43a5ba-18e3-4d58-8985-29ee1f7da40a.png)


Edit the file	
vi shop/src/main/java/com/shabushabu/javashop/shop/model/Instrument.java

Search for the method buildForLocale

/buildForLocale

Look at Code, notice the Annotation @WithSpan? @WithSpan is an [OpenTelemetry Annotation] (https://opentelemetry.io/docs/instrumentation/java/automatic/annotations/ ) for Java that automatically generates a span around a the function that follows.

@SpanAttribute is another [OpenTelemetry Annotation](https://opentelemetry.io/docs/instrumentation/java/automatic/annotations/ )that automatically adds a tag to a span with the corresponding parameter it annotates and its value. Using this technique we can tell developers exactly what the values of every parameter of a function the wrote or must repair at the time the problem occurred.

 ![Screen Shot 2022-11-28 at 7 45 13 AM](https://user-images.githubusercontent.com/32849847/204349143-1e35b6e4-4059-4c56-8718-76c14d41727c.png)


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

To save changes in vi type

:wq

Make sure you saved your changes to shop/src/main/java/com/shabushabu/javashop/shop/model/Instrument.java


# Build and Deploy Application

./BuildAndDeploy.sh

Waiting a few minutes.....

# Latency Root Cause
	
Open Service Map in Splunk Observability UI	

![Screen Shot 2022-12-08 at 11 48 58 AM](https://user-images.githubusercontent.com/32849847/206542093-f97b37ce-7e58-45bc-a281-5a388d60617e.png)


We can see we still have our original Latency issue, however our exception for Invalid Locale should be gone.

Let's check to see our InvalidLocale Exception is gone.

Click Shop Service

Click Traces on the right

We did remove the exception however it seems removing the Exception did not fix the latency...

Lets see if the newly added annotations provide us more relevant information for the next responder once we find the cause.

NOTE: We added additional information Parameter Values at Time of Latency. In this case the "Location" tag was created due our handy Annotator, that did the [OpenTelemetry Annotations](https://opentelemetry.io/docs/instrumentation/java/automatic/annotations/ ) for us.

![image](https://user-images.githubusercontent.com/32849847/213582624-66466a19-00fa-4dda-acd0-f6970d594ba1.png)
	
We can see that the actual function call that has the latency was not ProductResource.getAllProducts but 
the function call "ProductResource.myCoolFunction234234234" !

With this information a Developer can debug very quickly. 
	
Consider the case of debugging code, that you may not have written yourself without Parameter Values at the time of the issue ? You would have no choice but to go line .... by line ..... by line..... which can take a very long time. 

Since we now have the parameter tagged as part of our span metadata the actual root cause is seemingly related to the  "location" Colorado ! And it appears the one custom attribute that was tagged for function "ProductResource.myCoolFunction234234234" was "myInt" with a value of 999.



vi products/src/main/java/com/shabushabu/javashop/products/resources/ProductResource.java

search for 999
/999

We found and fixed the Needle In Haystack more quickly !!! 


Let's fix our code.

Enter i for insert

Change this:

private void myCoolFunction234234234(@SpanAttribute("myInt") int myInt) {
// Generate a FAST sleep of 0 time !
    	Random sleepy = new Random();
    	try{
        if (999==myInt) 
        Thread.sleep(
        sleepy.nextInt(5000 - 3000)
        + 3000);
        } catch (Exception e){
       
        }
    		
        }
	
	
    		
To this:

private void myCoolFunction234234234(@SpanAttribute("myInt") int myInt) {
// Generate a FAST sleep of 0 time !
    	Random sleepy = new Random();
   	try{
        if (999==myInt) 
      //  Thread.sleep(
      //  sleepy.nextInt(5000 - 3000)
      //  + 3000);
        } catch (Exception e){
       
        }
    		
        }
    

Save changes in vi type

:wq

Make sure you saved your changes to:  products/src/main/java/com/shabushabu/javashop/products/resources/ProductResource.java

    
# Build and Deploy Application

./BuildAndDeploy.sh

#Now that we have rebuilt and deployed our application, traffic is being sent once again.  

We are waiting a few minutes . . .


# If you do not see Red in your service map, you have successfully completed our Inventory application review for Shri Lanka and Colorado locations  !!

Now let's ensure Chicago was on-boarded correctly. However, since we have been having so many issues related to "location" and we have added that custom attribute via Opentelemetry Manual Instrumentation, lets go to the Splunk Observability UI and create an APM metric set around that tag. 

NOTE: We can do this without concern for Cardinality as we know this tag only has 6 possible values.

Index the location Tag.
	

![image](https://user-images.githubusercontent.com/32849847/213540265-5b0567ab-c9f3-412f-bec0-07277c7e8650.png)


# Build and Deploy Application to run traffic again
	
./BuildAndDeploy.sh 

Open a browser and navigate to http://localhost:8010

![image](https://user-images.githubusercontent.com/32849847/213541843-30266285-787f-493b-bc90-ffb4ac6e4c77.png)

	
Select a few locagtions and hit the login button, remember to select the Chicago Location and Login

Uhh ohh ! We received a 500 error, something is wrong there as well.  Return to the Splunk Observability UI and lets look once again at our Service Map

![Screen Shot 2022-11-28 at 8 11 47 AM](https://user-images.githubusercontent.com/32849847/204349595-fca270ad-379e-48c5-b2e1-7f222af82c55.png)
	
We see there was an un-handled exception thrown in Instruments service and some latency from our database.

Select the Instruments Service

Click on Traces on the right

Select "Errors Only" 
	
![Screen Shot 2022-11-28 at 8 14 50 AM](https://user-images.githubusercontent.com/32849847/204349696-dc19d62f-ed82-4533-ad27-138237821b8e.png)


We can see the exception was thrown by Hibernate, however it was thrown in our method 
"instruments: InstrumentRepository.findInstruments"
	
![Screen Shot 2022-11-28 at 8 14 37 AM](https://user-images.githubusercontent.com/32849847/204351905-03fe632b-b21c-4e8d-8044-dc582fed2253.png)


# Let's play developer again

Edit the file "instruments: InstrumentRepository.findInstruments"

vi instruments/src/main/java/com/shabushabu/javashop/instruments/repositories/FindInstrumentRepositoryImpl.java

	
![Screen Shot 2022-11-28 at 8 20 24 AM](https://user-images.githubusercontent.com/32849847/204349802-06135d9d-d70b-4cf1-aaea-3ba737e5c2b9.png)


We can see the developer accidently added the Instruments database with the Chicago Instruments database !

Let's change the query and fix this, remove "instruments_for_sale" from our Query

Change this:

public Object findInstruments() {
   LOGGER.info("findInstruments Called (All)");
    	
   Object obj = entityManager.createNativeQuery( "SELECT * FROM instruments_for_sale, instruments_for_sale_chicago").getResultList(); 
	 
   return obj;
}

To This:

public Object findInstruments() {
    	LOGGER.info("findInstruments Called (All)");
   // Object obj = entityManager.createNativeQuery( "SELECT * FROM instruments_for_sale, instruments_for_sale_chicago").getResultList(); 
   	
   Object obj = entityManager.createNativeQuery( "SELECT * FROM instruments_for_sale_chicago").getResultList(); 
	 
  return obj;
}
    

# Build and Deploy Application

cd javashop-otel directory

./BuildAndDeploy.sh

Now let's test the Chicago location once again 

Open a browser and navigate to http://localhost:8010

Select the Chicago Location and Login

We now see the 500 error is gone !

Let's confirm a clean Service Map 

![Screen Shot 2022-11-28 at 8 35 11 AM](https://user-images.githubusercontent.com/32849847/204350088-fca43e3c-42ea-4933-8a61-01eb2083fd23.png)

# If you see a clean service map, free of errors and Latency you have successfully completed the Java Instrumentation Workshop !




# Have a lovely day.


