
# OUR SCENARIO

Here at Buttercup Instruments, our business is expanding !  We have recently added 3 new locations, two locations in the USA Colorado and Chicago and one international location in SRI Lanka ! Our technical staff has already on-boarded the data from these new locations and incorporated them into our inventory application and it is our job
to review these improvements and send any issues back to our developers for repairs.

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
	
# Build and Deploy Application

cd "javashop-otel directory"

mvn clean install

docker-compose up -d --build 

# Traces will take a couple minutes ....

# View Service Map

If your instrumentation was successful, the service-map will show latency from the shop service to the products service.

![latency](https://user-images.githubusercontent.com/32849847/204347529-3bb439a7-3d7a-45c4-8fe2-3163f3a3fb8a.png)


Click on shop service

click Traces ( right side ) 

Sort by Duration

Select the longest duration trace

 Image![LongTrace](https://user-images.githubusercontent.com/32849847/204347798-4f232b7f-7a7a-483f-9d61-f0b535e9ecf0.png)


Now we can see the long latency occurred in the products service and if we click on products: /products
we can see the offending method was products:ProductResource.getAllProducts

![long-trace-detail-GetAllProducts](https://user-images.githubusercontent.com/32849847/204347855-724545bf-c3df-478a-a27d-e4f85f708e15.png)


Our next step here would be to send that trace to a developer by cliking download trace and
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

# Manual Instrumentation

To take a deeper look at this issue, we will implement manual instrumentation

To speed up manual instrumentation in Java you would leverage [OpenTelemetry Annotations](https://opentelemetry.io/docs/instrumentation/java/automatic/annotations/ )
, which automatically create a span around a method without modifying the actual code inside the method. 

To add even more information to help our developers find the root cause faster,
[OpenTelemetry Annotations](https://opentelemetry.io/docs/instrumentation/java/automatic/annotations/ ) can be used to generate span tags with parameter values for the method in question. 

It is important to remember that any developer should be able to debug a method with knowledge of parameter values at the time of an issue ( exception or latency ). 

To expedite manual instrumentation implementation for this exercise, we have provided a tool which will annotate the entirety of the "shop" and "products" services with OpenTelemetry standard annotations for every method call without having to write any code. 

# This Full-fidelity, Every-method approach is the Monolith Use Case with Splunk APM for Java.  


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

Look at Code, notice the Annotation @WithSpan? @WithSpan is an [OpenTelemetry Annotation]
(https://opentelemetry.io/docs/instrumentation/java/automatic/annotations/ ) for Java that automatically generates a span around a the function that follows.

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

Make sure you saved your changes to shop/src/main/java/com/shabushabu/javashop/shop/model/Instrument.java

To save changes in vi

:wq

#Rebuild and Deploy Application

docker-compose down

mvn clean install

docker-compose up -d --build 


Waiting a few minutes.....

# Latency Root Cause
	
Open Service Map in Splunk Observability UI	

![latency 2](https://user-images.githubusercontent.com/32849847/204349287-1f942776-05cf-443e-8b65-cc0f1b921416.png)


We can see we still have our original Latency issue, however our exception for Invalid Locale should be gone.

Let's check to see our InvalidLocale Exception is gone.

Click Shop Service

Click Traces on the right

We did remove the exception however it seems removing the Exception did not fix the latency...

Lets Look at the high latency traces causing this spike once again.

Also, lets see if these annotations provide us more relevant information for the next responder once we find the root cause.

NOTE: We added additional information Parameter Values at Time of Latency.

Developer can debug very quickly. 

Now we have the parameter tagged as part of our span metadata "location" Colorado must be the culprit ! 
We can also see that the actual method that has the latency was not ProductResource.getAllProducts but 
the method "products: ProductResource.myCoolFunction_withLocation"

vi products/src/main/java/com/shabushabu/javashop/products/resources/ProductResource.java

search for Colorado
/Colorado

We found and fixed the Needle In Haystack more quickly !!! 


Let's fix our code.

Enter i for insert

Change this:

if (location.equalsIgnoreCase("Colorado")) {

   // Generate a SLOW sleep of Random time !
   Random random = new Random();
    	  int sleepy = random.nextInt(5000 - 3000) + 3000;
    	  
   try{
    		  Thread.sleep(sleepy);
    		} catch (Exception e){
    		
   }
}
    		
To this:

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

save changes in vi

:wq
    
# Rebuild and Deploy Application

mvn clean install

docker-compose up -d --build 


#Now that we have rebuilt and deployed our application, traffic is being sent once again.  

We are waiting a few minutes . . .

Wait for it ... 

# If you do not see Red in your service map, you have successfully completed our Inventory application review for Shri Lanka and Colorado locations  !!

Now let's ensure Chicago was on-boarded correctly as well.

Open a browser and navigate to http://localhost:8010

Select the Chicago Location and Login

We received a 500 error, something is wrong there as well.  Return to the Splunk Observability UI and lets look 
once again at our Service Map

We see there was an unhandled exception thrown in Instruments service and some latency from our database.

Select the Instruments Service

Click on Traces on the right

Select "Errors Only" 
	
 ![Screen Shot 2022-11-28 at 8 11 47 AM](https://user-images.githubusercontent.com/32849847/204349595-fca270ad-379e-48c5-b2e1-7f222af82c55.png)


We can see the exception was thrown by Hibernate, however it was thrown in our method 
"instruments: InstrumentRepository.findInstruments"

# Let's play developer again

Edit the file "instruments: InstrumentRepository.findInstruments"

vi instruments/src/main/java/com/shabushabu/javashop/instruments/repositories/FindInstrumentRepositoryImpl.java

 ![Screen Shot 2022-11-28 at 8 14 50 AM](https://user-images.githubusercontent.com/32849847/204349696-dc19d62f-ed82-4533-ad27-138237821b8e.png)
	
	
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
    

# Rebuild and Deploy Application

docker-compose down 

mvn clean install
   
docker-compose up -d --build 

Now let's test the Chicago location once again 

Open a browser and navigate to http://localhost:8010

Select the Chicago Location and Login

We now see the 500 error is gone !

Let's confirm a clean Service Map 

![Screen Shot 2022-11-28 at 8 35 11 AM](https://user-images.githubusercontent.com/32849847/204350088-fca43e3c-42ea-4933-8a61-01eb2083fd23.png)


# If you see a clean service map, free of errors and Latency you have successfully completed the Java Instrumentation Workshop !

# Have a lovely day.

# End Workshop

