FROM openjdk:8-alpine

RUN addgroup -g 1000 app \
    && adduser -H -u 1000 -G app -s /bin/sh -D app \
    && sed -i 's/dl-cdn.alpinelinux.org/mirrors.ustc.edu.cn/g' /etc/apk/repositories \
    && apk add --no-cache curl

USER app

ENV ACTUATOR_USER=actuator
ENV ACTUATOR_PASSWORD=actuator

HEALTHCHECK --interval=5m --timeout=3s \
    CMD curl -f http://${ACTUATOR_USER}:${ACTUATOR_PASSWORD}@localhost:8080/actuator/health || exit 1

COPY --chown=app:app docker/start.sh /
ARG JAR_FILE
COPY --chown=app:app target/${JAR_FILE} /springboot-sample-demo.jar

ENTRYPOINT ["sh", "start.sh", "springboot-sample-demo.jar"]