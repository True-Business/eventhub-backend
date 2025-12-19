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
  forward-headers-strategy: framework

app:
  registration:
    cleanupJob:
      cron: {{ .Values.eventhubBackendDeploy.appConfig.registration.cleanupJob.cron }}
      zone: {{ .Values.eventhubBackendDeploy.appConfig.registration.cleanupJob.zone }}
    confirmationCodeExpirationMinutes: {{ .Values.eventhubBackendDeploy.appConfig.registration.confirmationCodeExpirationMinutes }}
  storage:
    cleanupJob:
      delay: {{ .Values.eventhubBackendDeploy.appConfig.storage.cleanupJob.delay }}
    minio:
      bucket:
        name: {{ .Values.eventhubBackendDeploy.appConfig.storage.minio.bucket.name }}
        uploadUrlConfirmExpiry: {{ .Values.eventhubBackendDeploy.appConfig.storage.minio.bucket.uploadUrlConfirmExpiry }}
        downloadUrlExpiry: {{ .Values.eventhubBackendDeploy.appConfig.storage.minio.bucket.downloadUrlExpiry }}
      user: {{ .Values.eventhubBackendDeploy.appConfig.storage.minio.user }}
      password: {{ .Values.eventhubBackendDeploy.appConfig.storage.minio.password }}
      url:
        internal: {{ .Values.eventhubBackendDeploy.appConfig.storage.minio.url.internal }}
        external: {{ .Values.eventhubBackendDeploy.appConfig.storage.minio.url.external }}

springdoc:
  swagger-ui:
    path: /api/event-hub
  api-docs:
    path: /v3/api-docs

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