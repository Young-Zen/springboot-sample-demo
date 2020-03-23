FROM openjdk:8-jdk-alpine

RUN addgroup -g 1000 app \
    && adduser -h /app -u 1000 -G app -s /bin/sh -D app

USER app

COPY --chown=app:app start.sh /
COPY --chown=app:app BOOT-INF/lib /app/lib
COPY --chown=app:app META-INF /app/META-INF
COPY --chown=app:app BOOT-INF/classes /app

ENTRYPOINT ["sh", "start.sh", "com.sz.springbootsample.demo.DemoApplication"]