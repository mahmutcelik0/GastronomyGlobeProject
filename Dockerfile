FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/backend-1.jar app.jar

ENTRYPOINT ["java", "-jar","/app.jar"]