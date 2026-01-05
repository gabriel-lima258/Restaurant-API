# imagem do java para o container
FROM eclipse-temurin:17-jre

# diretório de trabalho do container
WORKDIR /app

# copia o jar da aplicação para o container
COPY target/*.jar /app/api.jar

# porta que a aplicação irá rodar
EXPOSE 8080

# comando para executar a aplicação
CMD ["java", "-jar", "api.jar"]