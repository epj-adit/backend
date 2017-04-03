FROM openjdk:8-jdk

ARG SONAR_VERSION=3.0.0.702
ENV SONAR_VERSION=$SONAR_VERSION
ARG NODE_ENV=dev
ENV NODE_ENV=$NODE_ENV
ARG M2_HOME=/opt/apache-maven
ENV M2_HOME=$M2_HOME

ARG POSTGRES_DB=adit
ENV POSTGRES_DB=$POSTGRES_DB
ARG POSTGRES_URL=jdbc:postgresql://engineering-projekt-server-testing-postgres:5432/$POSTGRES_DB
ENV POSTGRES_URL=$POSTGRES_URL
ARG POSTGRES_USER=adit
ENV POSTGRES_USER=$POSTGRES_USER
ARG POSTGRES_PASSWORD=adit
ENV POSTGRES_PASSWORD=$POSTGRES_PASSWORD

RUN useradd -m -u 1000 java

#RUN apk add --no-cache maven unzip openssl
RUN apt-get install -y unzip openssl

# Install maven
ARG MAVEN_VERSION=3.3.9
RUN cd /opt && wget -q http://mirror.switch.ch/mirror/apache/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz \
    && tar -xvzf apache-maven-${MAVEN_VERSION}-bin.tar.gz && rm apache-maven-${MAVEN_VERSION}-bin.tar.gz && mv apache-maven-${MAVEN_VERSION} apache-maven

# Add sonar scanner
RUN cd /opt && wget -q https://sonarsource.bintray.com/Distribution/sonar-scanner-cli/sonar-scanner-cli-${SONAR_VERSION}-linux.zip \
    && unzip sonar-scanner-cli-${SONAR_VERSION}-linux.zip && rm sonar-scanner-cli-${SONAR_VERSION}-linux.zip && mv sonar-scanner-${SONAR_VERSION}-linux sonar-scanner
ENV PATH="/opt/sonar-scanner/bin:/opt/apache-maven/bin:${PATH}"

USER java
VOLUME [ "/home/java/.m2" ]
VOLUME [ "/home/java/project" ]
WORKDIR /home/java/project
