# Dockerfile

FROM openjdk:11

WORKDIR /app

# Copia os arquivos fonte
COPY src/ ./src/

# Compila os arquivos Java
RUN javac src/*.java -d out

# Define o comando padrão
CMD ["java", "-cp", "out", "Main"]

