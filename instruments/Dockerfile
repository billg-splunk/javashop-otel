FROM openjdk:8-jre
ADD target/javashop.instruments-latest.jar app.jar
EXPOSE 8040


ADD https://github.com/signalfx/splunk-otel-java/releases/latest/download/splunk-otel-javaagent-all.jar .

ENV JAVA_OPTS="-Xms2048M -Xmx2048M"

ENTRYPOINT java $JAVA_OPTS -javaagent:splunk-otel-javaagent-all.jar -Dotel.resource.attributes=service.name=instruments,deployment.environment=${USERNAME}22_Apm_Instrumentation_Shop -jar app.jar

# Test Start: ENTRYPOINT java $JAVA_OPTS -Dotel.resource.attributes=service.name=instruments,deployment.environment=${USERNAME}22_Apm_Instrumentation_Shop -jar app.jar
