#!/bin/bash

# Passo 8: Inicie o FrontEnt da aplicação
echo "Inicializando WebApp"
docker-compose up --build -d webapp

echo "Aplicações iniciadas com sucesso!"
