# syntax=docker/dockerfile:1
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /build

COPY pom.xml .
COPY api-gateway/pom.xml api-gateway/pom.xml
RUN mvn -f api-gateway/pom.xml -DskipTests dependency:go-offline -q || true

COPY api-gateway/src api-gateway/src
RUN mvn -f api-gateway/pom.xml -DskipTests package -q

FROM eclipse-temurin:21-jre
RUN apt-get update && apt-get install -y --no-install-recommends curl && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY --from=build /build/api-gateway/target/api-gateway-1.0.0.jar app.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "app.jar"]
