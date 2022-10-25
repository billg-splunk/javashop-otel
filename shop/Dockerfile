FROM openjdk:8-jre
ADD target/javashop.shop-latest.jar ./app.jar
EXPOSE 8010

#EXPOSE 8001

ADD https://github.com/signalfx/splunk-otel-java/releases/latest/download/splunk-otel-javaagent-all.jar .

#ENTRYPOINT java -agentlib:jdwp=transport=dt_socket, server=y, suspend=n, address=*:8001 -javaagent:splunk-otel-javaagent-all.jar  -Dotel.resource.attributes=service.name=shop,deployment.environment=${USERNAME}_Apm_Instrumentation_Shop -jar app.jar

ENTRYPOINT java -javaagent:splunk-otel-javaagent-all.jar  -Dotel.resource.attributes=service.name=shop,deployment.environment=${USERNAME}_Apm_Instrumentation_Shop -jar app.jar
