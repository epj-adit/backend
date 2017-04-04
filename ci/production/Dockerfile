FROM openjdk:8-alpine

RUN adduser -D -u 1000 java

EXPOSE 4567

USER java
WORKDIR /home/java

ADD target/adit-*-with-dependencies.jar /home/java/adit.jar
CMD java -jar /home/java/adit.jar ch.hsr.adit.application.app.App
