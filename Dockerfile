FROM eclipse-temurin:25-jre
EXPOSE 8080
RUN apt-get update && apt-get install -y --no-install-recommends ca-certificates && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY backend/target/app.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
