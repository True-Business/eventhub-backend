spring:
  application:
    name: eventhub-backend
server:
  port: {{ .Values.eventhubBackendDeploy.service.port }}

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