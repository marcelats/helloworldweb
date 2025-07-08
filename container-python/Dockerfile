FROM python:3.11-slim

WORKDIR /app

COPY app.py .

RUN apt-get update && apt-get install -y \
    default-jdk \
    r-base \
    && apt-get clean

RUN pip install flask simpy

CMD ["python", "app.py"]
FROM python:3.11-slim

WORKDIR /app

COPY app.py .

# Instala dependências do sistema, incluindo R e Java
RUN apt-get update && apt-get install -y \
    default-jdk \
    r-base \
    && apt-get clean

# Instala bibliotecas Python
RUN pip install flask simpy

# Instala a biblioteca 'simmer' do R
RUN R -e "install.packages('simmer', repos='https://cloud.r-project.org')"

CMD ["python", "app.py"]

