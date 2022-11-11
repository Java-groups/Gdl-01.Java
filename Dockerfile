FROM openjdk:8-jdk-alpine
COPY  "./target/sport-hub.jar" "sport-hub-docker.jar"
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/sport-hub-docker.jar"]