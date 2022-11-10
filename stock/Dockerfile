FROM openjdk:8-jre
ADD target/javashop.stock-latest.jar app.jar
EXPOSE 8030

ADD https://github.com/signalfx/splunk-otel-java/releases/latest/download/splunk-otel-javaagent-all.jar .
ENTRYPOINT java -javaagent:splunk-otel-javaagent-all.jar -Dotel.resource.attributes=service.name=stock,deployment.environment=${USERNAME}22_Apm_Instrumentation_Shop -jar app.jar

# TEST START: ENTRYPOINT java -Dotel.resource.attributes=service.name=stock,deployment.environment=${USERNAME}2_Apm_Instrumentation_Shop -jar app.jar
