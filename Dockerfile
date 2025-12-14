FROM maven:3.9-eclipse-temurin-21-alpine AS build

WORKDIR /build

COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-alpine
WORKDIR /app
COPY minio.crt /minio.crt
RUN keytool -import -alias minio -file /minio.crt -keystore $JAVA_HOME/lib/security/cacerts -storepass changeit -noprompt
COPY --from=build /build/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
