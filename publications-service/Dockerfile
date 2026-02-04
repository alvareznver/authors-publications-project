FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8002
ENV DB_HOST=db-publications \
    DB_PORT=5432 \
    DB_NAME=publications_db \
    DB_USER=postgres \
    DB_PASSWORD=postgres \
    AUTHORS_SERVICE_URL=http://authors-service:8001

HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
  CMD java -cp app.jar org.springframework.boot.loader.JarLauncher && \
      curl -f http://localhost:8002/api/v1/publications/stats/total || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
