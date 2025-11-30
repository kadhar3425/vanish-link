# Dockerfile
FROM openjdk:17-jdk-slim

# App jar copy pannum bodhu fast aagum
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Port expose
EXPOSE 8080

# Run command
ENTRYPOINT ["java","-jar","/app.jar"]