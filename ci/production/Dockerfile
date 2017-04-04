FROM openjdk:8-alpine

RUN adduser -D -u 1000 java

ENV POSTGRES_URL=""
ARG POSTGRES_DB=adit
ENV POSTGRES_DB=$POSTGRES_DB
ARG POSTGRES_USER=adit
ENV POSTGRES_USER=$POSTGRES_USER
ENV POSTGRES_PASSWORD=""

EXPOSE 4567

USER java
WORKDIR /home/java

ADD target/adit-*-with-dependencies.jar /home/java/adit.jar
CMD java -jar /home/java/adit.jar ch.hsr.adit.application.app.App
