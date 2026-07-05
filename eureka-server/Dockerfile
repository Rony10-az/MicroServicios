# syntax=docker/dockerfile:1
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /build

COPY pom.xml .
COPY eureka-server/pom.xml eureka-server/pom.xml
RUN mvn -f eureka-server/pom.xml -DskipTests dependency:go-offline -q || true

COPY eureka-server/src eureka-server/src
RUN mvn -f eureka-server/pom.xml -DskipTests package -q

FROM eclipse-temurin:21-jre
RUN apt-get update && apt-get install -y --no-install-recommends curl && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY --from=build /build/eureka-server/target/eureka-server-1.0.0.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]
