FROM openjdk:17-jdk-slim

ARG JAR_FILE=build/**/clematis.mt.api-*.jar
COPY ${JAR_FILE} app.jar

# Import Keycloak Cert to JRE cacerts
COPY jenkins/keycloak.pem /tmp/keycloak.pem
RUN keytool -importcert -file /tmp/keycloak.pem -alias keycloak -cacerts -storepass changeit -noprompt

RUN mkdir -p /var/log/clematis

ENTRYPOINT ["java","-jar","/app.jar"]
