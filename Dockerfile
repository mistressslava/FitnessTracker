FROM eclipse-temurin:25-jre
EXPOSE 8080
WORKDIR /app
COPY backend/target/app.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
