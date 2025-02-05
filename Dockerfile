# Usar a imagem oficial do Payara (fork do GlassFish) como base
FROM payara/server-full:latest

# Definir o diretório de trabalho dentro do container
WORKDIR /opt/payara

# Copiar o arquivo WAR da aplicação para o diretório de deploy do Payara
COPY WebApplication1.war /opt/payara/deployments/

# Expor a porta 8080 (porta padrão do GlassFish e Payara para aplicações web)
EXPOSE 8080

# Comando para iniciar o GlassFish/Payara
CMD ["bin/asadmin", "start-domain", "--verbose"]
