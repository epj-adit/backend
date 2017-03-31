FROM openjdk:8-jdk-alpine

ARG SONAR_VERSION=3.0.0.702
ENV SONAR_VERSION=$SONAR_VERSION
ARG NODE_ENV=dev
ENV NODE_ENV=$NODE_ENV

ARG POSTGRES_DB=adit
ENV POSTGRES_DB=$POSTGRES_DB
ARG POSTGRES_URL=jdbc:postgresql://engineering-projekt-server-testing-postgres:5432/$POSTGRES_DB
ENV POSTGRES_URL=$POSTGRES_URL
ARG POSTGRES_USER=adit
ENV POSTGRES_USER=$POSTGRES_USER
ARG POSTGRES_PASSWORD=adit
ENV POSTGRES_PASSWORD=$POSTGRES_PASSWORD

RUN adduser -D -u 1000 java

RUN apk add --no-cache maven unzip openssl

# Add sonar scanner
RUN mkdir /opt && cd /opt && wget -q https://sonarsource.bintray.com/Distribution/sonar-scanner-cli/sonar-scanner-cli-${SONAR_VERSION}-linux.zip \
    && unzip sonar-scanner-cli-${SONAR_VERSION}-linux.zip && rm sonar-scanner-cli-${SONAR_VERSION}-linux.zip && mv sonar-scanner-${SONAR_VERSION}-linux sonar-scanner
RUN /bin/sh -c 'echo "export PATH=/opt/sonar-scanner/bin:\$PATH" >> /etc/profile'

USER java
VOLUME [ "/home/java/.m2" ]
VOLUME [ "/home/java/project" ]
WORKDIR /home/java/project
