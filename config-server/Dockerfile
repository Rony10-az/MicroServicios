# syntax=docker/dockerfile:1
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /build

COPY pom.xml .
COPY config-server/pom.xml config-server/pom.xml
RUN mvn -f config-server/pom.xml -DskipTests dependency:go-offline -q || true

COPY config-server/src config-server/src
RUN mvn -f config-server/pom.xml -DskipTests package -q

FROM eclipse-temurin:21-jre
RUN apt-get update && apt-get install -y --no-install-recommends curl && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY --from=build /build/config-server/target/config-server-1.0.0.jar app.jar
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "app.jar"]
