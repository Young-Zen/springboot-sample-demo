#!/bin/bash
if [ -z $2 ]; then
    rm -rf BOOT-INF;
    rm -rf META-INF;
    rm -rf org;
    cd ../..;
    mvn clean package;
    cd docker/manual;
fi
projectName=springboot-sample-demo;
jar -xf ../../target/${projectName}.jar;
if [ $# -ge 1 ]; then
    docker build -t baiduyun.com/${projectName}:$1 .;
fi