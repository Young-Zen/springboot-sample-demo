FROM eclipse-temurin:8u452-b09-jdk-focal

RUN groupadd -r -g 1000 app \
    && useradd -m -d /home/app/ -s /bin/bash -u 1000 -r -g app app \
    && sed -i 's/deb.debian.org/mirrors.ustc.edu.cn/g' /etc/apt/sources.list \
    && apt-get update \
    && apt install -y --no-install-recommends procps inetutils-ping net-tools curl vim \
    && rm -rf /var/lib/apt/lists/*

USER app

COPY --chown=app:app start.sh /
COPY --chown=app:app BOOT-INF/lib /app/lib
COPY --chown=app:app META-INF /app/META-INF
COPY --chown=app:app BOOT-INF/classes /app

ENTRYPOINT ["bash", "start.sh", "com.sz.springbootsample.demo.DemoApplication"]