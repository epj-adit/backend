FROM openjdk:8-jdk-alpine

ARG SONAR_VERSION=3.0.0.702
ENV SONAR_VERSION=$SONAR_VERSION
ARG NODE_ENV=dev
ENV NODE_ENV=$NODE_ENV

RUN adduser -D -u 1000 java

RUN apk add --no-cache maven unzip openssl

# Add sonar scanner
RUN mkdir /opt && cd /opt && wget -q https://sonarsource.bintray.com/Distribution/sonar-scanner-cli/sonar-scanner-cli-${SONAR_VERSION}-linux.zip \
    && unzip sonar-scanner-cli-${SONAR_VERSION}-linux.zip && rm sonar-scanner-cli-${SONAR_VERSION}-linux.zip && mv sonar-scanner-${SONAR_VERSION}-linux sonar-scanner
RUN /bin/sh -c 'echo "export PATH=/opt/sonar-scanner/bin:\$PATH" >> /etc/profile'

USER java
VOLUME [ "/home/java/project" ]
WORKDIR /home/java/project
