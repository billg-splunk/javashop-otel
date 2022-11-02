FROM openjdk:8-jre
ADD target/javashop.instruments-latest.jar app.jar
EXPOSE 8040


ADD https://github.com/signalfx/splunk-otel-java/releases/latest/download/splunk-otel-javaagent-all.jar .


ENTRYPOINT java -javaagent:splunk-otel-javaagent-all.jar -Dotel.resource.attributes=service.name=instruments,deployment.environment=${USERNAME}_Apm_Instrumentation_Shop -jar app.jar
