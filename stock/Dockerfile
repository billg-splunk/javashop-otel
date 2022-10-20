FROM openjdk:8-jre
ADD target/javashop.stock-latest.jar app.jar
EXPOSE 8030


#COPY splunk-otel-javaagent-all.jar .
ADD https://github.com/signalfx/splunk-otel-java/releases/latest/download/splunk-otel-javaagent-all.jar .


ENTRYPOINT ["java","-javaagent:splunk-otel-javaagent-all.jar", "-Dotel.resource.attributes=service.name=stock,deployment.environment=Apm_Instrumentation_Shop","-Dotel.exporter.otlp.endpoint=http://172.31.17.42:4317", "-jar", "app.jar"]

