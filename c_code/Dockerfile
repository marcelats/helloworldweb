# Etapa 1: build com Maven
FROM maven:3.8.6-openjdk-11-slim AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package
# Dockerfile

FROM openjdk:11-jre-slim

WORKDIR /app

# Copia os arquivos fonte
COPY --from=builder /app/target/meu-projeto-1.0-SNAPSHOT-jar-with-dependencies.jar app.jar

EXPOSE 8002

# Define o comando padrão
CMD ["java", "-jar", "app.jar"]

