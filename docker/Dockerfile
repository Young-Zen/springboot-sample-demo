FROM openjdk:8-jdk-alpine

ARG JAR_FILE
COPY docker/start.sh /
COPY target/${JAR_FILE} /springboot-sample-demo.jar

ENTRYPOINT ["sh", "start.sh", "springboot-sample-demo.jar"]