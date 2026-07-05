# syntax=docker/dockerfile:1
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /build

COPY pom.xml .
COPY ms-reservas/pom.xml ms-reservas/pom.xml
RUN mvn -f ms-reservas/pom.xml -DskipTests dependency:go-offline -q || true

COPY ms-reservas/src ms-reservas/src
RUN mvn -f ms-reservas/pom.xml -DskipTests package -q

FROM eclipse-temurin:21-jre
RUN apt-get update && apt-get install -y --no-install-recommends curl && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY --from=build /build/ms-reservas/target/ms-reservas-1.0.0.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]
