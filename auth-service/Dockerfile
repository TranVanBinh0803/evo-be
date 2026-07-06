FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} auth-service.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","auth-service.jar"]
