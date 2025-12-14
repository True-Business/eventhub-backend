FROM maven:3.9-eclipse-temurin-21-alpine AS build

WORKDIR /build

COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-alpine
WORKDIR /app

ARG MINIO_PUBLIC_CERT
RUN echo "$MINIO_PUBLIC_CERT" > /minio.crt && \
    keytool -import -alias minio -file /minio.crt -keystore $JAVA_HOME/lib/security/cacerts -storepass changeit -noprompt && \
    rm /minio.crt

COPY --from=build /build/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
