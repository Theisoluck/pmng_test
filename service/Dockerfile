# Etapa 1: compila el jar con Maven y Java 21 (version del pom.xml)
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn -B -q dependency:go-offline

COPY src ./src
# Las pruebas necesitan una base de datos real, por eso no corren durante la construccion
RUN mvn -B -q package -DskipTests

# Etapa 2: imagen final solo con el JRE y el jar
FROM eclipse-temurin:21-jre
WORKDIR /app

RUN groupadd --system app && useradd --system --gid app app
COPY --from=build /app/target/service-0.0.1-SNAPSHOT.jar app.jar
USER app

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
