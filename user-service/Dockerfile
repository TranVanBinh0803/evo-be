FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} user-service.jar
EXPOSE 4046
ENTRYPOINT ["java","-jar","user-service.jar"]
