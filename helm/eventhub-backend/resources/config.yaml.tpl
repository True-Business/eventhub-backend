spring:
  application:
    name: eventhub-backend
server:
  port: {{ .Values.eventhubBackendDeploy.service.port }}
