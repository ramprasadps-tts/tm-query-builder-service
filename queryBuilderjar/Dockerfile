FROM openjdk:17-jdk-slim
WORKDIR /app
COPY tm-query-builder-service.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
EXPOSE 8081

