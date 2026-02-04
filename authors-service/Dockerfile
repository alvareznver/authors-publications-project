FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8001
ENV DB_HOST=db-authors \
    DB_PORT=5432 \
    DB_NAME=authors_db \
    DB_USER=postgres \
    DB_PASSWORD=postgres

HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
  CMD java -cp app.jar org.springframework.boot.loader.JarLauncher && \
      curl -f http://localhost:8001/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
