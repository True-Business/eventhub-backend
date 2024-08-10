FROM eclipse-temurin:21.0.2_13-jre-alpine

COPY target/*.jar ./application.jar

ENTRYPOINT ["java","-jar","application.jar"]