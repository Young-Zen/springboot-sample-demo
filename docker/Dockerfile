FROM openjdk:8-jdk-slim

RUN groupadd -r -g 1000 app \
    && useradd -m -d /home/app/ -s /bin/bash -u 1000 -r -g app app \
    && sed -i 's/deb.debian.org/mirrors.ustc.edu.cn/g' /etc/apt/sources.list \
    && apt-get update \
    && apt install -y --no-install-recommends procps inetutils-ping net-tools curl vim \
    && rm -rf /var/lib/apt/lists/*

USER app

ENV ACTUATOR_USER=actuator
ENV ACTUATOR_PASSWORD=actuator

HEALTHCHECK --interval=5m --timeout=3s \
    CMD curl -f http://${ACTUATOR_USER}:${ACTUATOR_PASSWORD}@localhost:8080/actuator/health || exit 1

COPY --chown=app:app docker/start.sh /
ARG JAR_FILE
COPY --chown=app:app target/${JAR_FILE} /springboot-sample-demo.jar

ENTRYPOINT ["bash", "start.sh", "springboot-sample-demo.jar"]