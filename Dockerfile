# JDK 25 na construção. A tag acompanha a imagem oficial do Maven com Temurin.
# https://hub.docker.com/_/maven
FROM maven:3.9.11-eclipse-temurin-25 AS build

WORKDIR /usr/src/app

COPY pom.xml .
COPY lab-config lab-config
COPY lab-core lab-core
COPY lab-dataprovider lab-dataprovider
COPY lab-entrypoint lab-entrypoint

RUN mvn -B clean package -DskipTests

# JRE 25 no runtime.
# https://hub.docker.com/_/eclipse-temurin
FROM eclipse-temurin:25-jre AS release
LABEL maintainer="Projeto Clean Architecture com AWS"
WORKDIR /opt/app

COPY --from=build /usr/src/app/lab-entrypoint/target/lab-entrypoint-1.0.0.jar app.jar

EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/opt/app/app.jar"]
