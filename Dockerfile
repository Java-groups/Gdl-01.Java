FROM openjdk:8-jdk-alpine
COPY  "./target/sport-hub.jar" "sport-hub-docker-ex.jar"
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/sport-hub-docker-ex.jar"]