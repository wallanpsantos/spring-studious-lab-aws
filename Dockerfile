# Etapa de construção (build)
FROM maven:3.9.9-eclipse-temurin-21-alpine AS build

# Copia os módulos e o POM principal
COPY lab-core /usr/src/app/lab-core
COPY lab-dataprovider /usr/src/app/lab-dataprovider
COPY lab-entrypoint /usr/src/app/lab-entrypoint
COPY pom.xml /usr/src/app/pom.xml

# Força o Maven a atualizar dependências e a resolver o parent POM
RUN mvn -f /usr/src/app/pom.xml clean package -DskipTests -U

# Etapa de execução (runtime)
FROM eclipse-temurin:21-alpine AS release
LABEL maintainer="Projeto Clean Architecture com AWS"
WORKDIR /opt/app

# Copia o JAR construído para o ambiente de runtime
COPY --from=build /usr/src/app/lab-entrypoint/target/*.jar spring-studious-lab-aws.jar

EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/opt/app/spring-studious-lab-aws.jar"]