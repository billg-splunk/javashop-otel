FROM openjdk:8-jre
ADD target/javashop.products-latest.jar app.jar
ADD products.yml app-config.yml
EXPOSE 8020

ADD https://github.com/signalfx/splunk-otel-java/releases/latest/download/splunk-otel-javaagent-all.jar .
ENV JAVA_OPTS="-XX:PermSize=512m -XX:MaxPermSize=512m"
ENTRYPOINT java -javaagent:splunk-otel-javaagent-all.jar  -Dotel.resource.attributes=service.name=products,deployment.environment=${USERNAME}2_Apm_Instrumentation_Shop -jar app.jar  server app-config.yml

