spring:
  application:
    name: eventhub-backend
  datasource:
    url: ${DB_URL}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  liquibase:
    change-log: classpath:db.changelog/db.changelog-master.xml
  jpa:
    properties:
      hibernate:
        physical_naming_strategy: org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy
  mail:
    host: smtp.mail.ru
    port: 465
    username: ${EMAIL_LOGIN}
    password: ${MAIL_APPLICATION_PASSWORD}
    properties:
      mail:
        smtp:
          auth: true
          ssl:
            enable: true
          starttls:
            enable: false
server:
  port: {{ .Values.eventhubBackendDeploy.service.port }}
app:
  registration:
    cleanupJob:
      cron: {{ .Values.eventhubBackendDeploy.appConfig.registration.cleanupJob.cron }}
      zone: {{ .Values.eventhubBackendDeploy.appConfig.registration.cleanupJob.zone }}
    confirmationCodeExpirationMinutes: {{ .Values.eventhubBackendDeploy.appConfig.registration.confirmationCodeExpirationMinutes }}

management:
  endpoint:
    health:
      probes:
        enabled: true
      access: unrestricted
  health:
    livenessState:
      enabled: true
    readinessState:
      enabled: true
  endpoints:
    web:
      exposure:
        include: "*"