FROM jetty:9.4-jre8-alpine

ARG JAVA_OPTIONS="-Xmx500m"
ENV JAVA_OPTIONS=${JAVA_OPTIONS}

# Enable http2
RUN java -jar ${JETTY_HOME}/start.jar --add-to-startd=http2 --approve-all-licenses

# Add application
ADD build.war /var/lib/jetty/webapps/

EXPOSE 8080

USER jetty
WORKDIR ${JETTY_HOME}
