# Stage 1: build the backend
FROM gradle:8.10-jdk17 AS builder
WORKDIR /opt/app
COPY . .
RUN ./gradlew clean build -x test

# Stage 2: run the backend
FROM eclipse-temurin:17-jre-jammy
WORKDIR /opt/app
COPY --from=builder /opt/app/build/libs/*.jar /opt/app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/opt/app/app.jar"]