# Dockerfile (Fixed for Render - Full Maven Build Inside)

# Step 1: Maven build stage
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Step 2: Runtime stage
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /build/target/*.jar app.jar

EXPOSE 8080

ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]