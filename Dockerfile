# FROM maven:3.9-eclipse-temurin-21-alpine AS build
#
# WORKDIR /build
#
# COPY . .
# RUN mvn clean package -DskipTests
#
# FROM eclipse-temurin:21-alpine
# WORKDIR /app
# COPY --from=build /build/target/*.jar app.jar
#
# ENTRYPOINT ["java", "-jar", "app.jar"]

FROM eclipse-temurin:21-alpine AS builder

WORKDIR /app
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract


FROM eclipse-temurin:21-alpine

WORKDIR /app
COPY --from=builder /app/dependencies/ ./
COPY --from=builder /app/spring-boot-loader/ ./
COPY --from=builder /app/snapshot-dependencies/ ./
COPY --from=builder /app/application/ ./

CMD [ "java", "org.springframework.boot.loader.launch.JarLauncher" ]