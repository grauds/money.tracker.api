FROM openjdk:11-jdk
ARG JAR_FILE=build/**/mt-*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
