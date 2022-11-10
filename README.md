# Tested on Macos

# You will  need:
Git, Maven, Docker 

# Set Environment Variables

Environment Variables:

export USERNAME=<Your-UserName>
export SPLUNK_ACCESS_TOKEN=<Your-Token>
export SPLUNK_REALM=<Your-Realm>

# Steps

# Git clone
git clone -b workshop https://github.com/shabuhabs/javashop-otel.git
	
cd javashop-otel

# First we Implement Auto-Instrumentation via Dockerfiles


cd shop
vi Dockerfile

add Otel Java Agent to Java ENTRYPOINT,  ( java -javaagent:splunk-otel-javaagent-all.jar 
See examples at ../Dockerfiles_Instrumented

repeat for:
	instruments / products / stock  
	
#Build Application

cd <javashop-otel> directory

mvn clean install

# Run Application

docker-compose up -d --build


# Traces will take a couple minutes 

# View Service Map

If your instrumentation was successful, the service-map will show latency from the shop service to the products service.

# Manual Instrumentation

To take a deeper look at this issue, we will implement manual instrumentation

Normally, you would leverage Annnotations, https://opentelemetry.io/docs/instrumentation/java/automatic/annotations/ which automatically 
create a span for a function. In addition, Annotations can be used to generate span tags, for parameter values for the function in question.

To expedite this implementation, we have provided a program which will annotate the entire "shop" service directory for you. 

# Run the Annotator

cd annotator

mvn exec:java

#Now let's run the test traffic once again and let's see if these annotations help us narrow down the root cause more quickly.
Additionally, let's also see if these annotations provide us more useable information for the next responder once we find the root cause.






kubectl get svc

#take note of the port for the shop service use that as SHOP_SERVICE_PORT below

#Open in browser - t http://<YOUR_NODE_IP>:<SHOP_SERVICE_PORT>/?name=MyName&color=Blue

# If you want to build it . . .Repeat steps for products and stock.

cd shop

Mvn clean install

docker build -t YourNameHere/shabu-shop:1.0 .

#login to docker

docker push YourNameHere/shabu-shop:1.0

vi shop/shop.yaml # Add your image name above YourNameHere/shabu-shop:1.0 to the shop.yaml file.

kubectl apply -f shop/shop.yaml

