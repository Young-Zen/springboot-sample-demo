FROM openjdk:8-jdk-alpine

COPY start.sh /
COPY BOOT-INF/lib /app/lib
COPY META-INF /app/META-INF
COPY BOOT-INF/classes /app

ENTRYPOINT ["sh", "-c", "start.sh", "com.sz.springbootsample.demo.DemoApplication"]