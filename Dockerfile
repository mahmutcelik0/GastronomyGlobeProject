FROM ubuntu:latest AS build
RUN apt-get update
RUN apt-get install openjdk-17-jdk -y
COPY . .

WORKDIR /app

COPY pom.xml .
FROM maven:3.6-jdk-11 as maven_build
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar

EXPOSE 8080

COPY --from=build /target/backend-1.jar app.jar

ENTRYPOINT ["java", "-jar","/app.jar"]