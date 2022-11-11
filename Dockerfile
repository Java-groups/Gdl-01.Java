FROM openjdk:8-jdk-alpine
COPY  "./target/sport-hub.jar" "sport-hub-docker.jar"
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/sport-hub-docker.jar"]
CMD ["/home/sport-hub-11400-firebase-adminsdk-vvbtu-f8395fca03.json"]