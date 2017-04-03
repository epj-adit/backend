FROM openjdk:8-alpine

ARG JAVA_OPTIONS="-Xmx500m"
ENV JAVA_OPTIONS=${JAVA_OPTIONS}

RUN adduser -D -u 1000 java

EXPOSE 8080

USER java
WORKDIR /home/java

ADD target/adit-*.jar /home/java/adit.jar
CMD [ "java -jar  adit.jar ${JAVA_OPTIONS}" ]
