FROM openjdk:8-jre

ARG JAR_FILE
COPY target/${JAR_FILE} /springboot-sample-demo.jar
COPY docker/start.sh /

ENTRYPOINT ["sh", "start.sh", "springboot-sample-demo.jar"]