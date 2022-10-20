FROM openjdk:8-jre
ADD target/javashop.products-latest.jar app.jar
ADD products.yml app-config.yml
EXPOSE 8020


ADD https://github.com/signalfx/splunk-otel-java/releases/latest/download/splunk-otel-javaagent-all.jar .

#ENTRYPOINT ["java","-javaagent:splunk-otel-javaagent-all.jar", "-Dotel.resource.attributres=serice.name=products .jaeger.service.name=products", "-jar","app.jar", "server", "app-config.yml"]

#ENTRYPOINT ["java","-javaagent:splunk-otel-javaagent-all.jar", "-Dotel.resource.attributes=service.name=products,deployment.environment=Apm_Instrumentation_Shop","-Dotel.exporter.otlp.endpoint=http://172.31.17.42:4317", "server", "app-config.yml", "-jar", "app.jar"]

ENTRYPOINT java -javaagent:splunk-otel-javaagent-all.jar  -Dotel.resource.attributes=service.name=products,deployment.environment=Apm_Instrumentation_Shop -Dotel.exporter.otlp.endpoint=http://172.31.17.42:4317 -jar app.jar  server app-config.yml



#ENTRYPOINT ["java","-javaagent:splunk-otel-javaagent-all.jar", "-Dotel.exporter.jaeger.service.name=products,environment=${USERNAME}-Apm-Instrumentation_Shop -Dotel.exporter.jaeger.endpoint=https://localhost:9080//v1//trace", "-jar","app.jar", "server", "app-config.yml"]

