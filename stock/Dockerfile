FROM openjdk:8-jre
ADD target/javashop.stock-latest.jar app.jar
EXPOSE 8030


#COPY splunk-otel-javaagent-all.jar .
ADD https://github.com/signalfx/splunk-otel-java/releases/latest/download/splunk-otel-javaagent-all.jar .
ENV JAVA_OPTS="-XX:PermSize=1024m -XX:MaxPermSize=1024m"

ENTRYPOINT java -javaagent:splunk-otel-javaagent-all.jar -Dotel.resource.attributes=service.name=stock,deployment.environment=${USERNAME}2_Apm_Instrumentation_Shop -jar app.jar
