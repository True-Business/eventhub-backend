# helm chart eventhub-backend

Используется для деплоя бекенда в kubernetes.

## Содержимое

```
resources/
    config.yaml.tpl     # шаблон для конфигурационного файла application.yaml
templates/              # шаблоны манифестов для kubernetes
    deployment.yaml     # шаблон для деплоймента приложения, здесь описываются задаются ENV переменные
Chart.yaml              # базовая информация о helm chart
minio.values.yaml       # значения для шаблонов стороннего minio helm chart-а
postgres.values.yaml    # значения для шаблонов стороннего postgres helm chart-а
values.yaml             # значения для шаблонов eventhub-backend 
```

## Как добавить поля в application.yaml для приложения в кубе

1. Ответьте на вопросы:
   1. Будут ли меняться эти поля в зависимости от окружения? 
   2. Содержат ли они секреты?
   3. Используется ли значение поля в нескольких местах?
2. Если на все вопросы дан отрицательный ответ:
   1. Зайдите в `resources/config.yaml.tpl` и пропишите там желаемые поля. Пример:
   ```yaml
   spring:
     application:
       name: eventhub-backend
   ```
3. Если на хотя бы один вопрос ответили да:
   1. Прописываем в `values.yaml` желаемую иерархию полей (если значение поля - секрет (пароль, токен и т.п.), то в значение поля пишем какую-нибудь билиберду и пишем коммент в файле, что значение задётся в ci). Пример (уже есть в `values.yaml`) - конфигурация порта для приложения:
   ```yaml
   eventhubBackendDeploy:
     service:
       port: 8080
   ```
   2. В шаблон конфига `resources/config.yaml.tpl` прописываем иерархию полей, требуемую приложением, значения полей задаём через `{{ .Values }}`. Пример (уже есть в `resources/config.yaml.tpl`) - конфигурация порта для приложения:
   ```yaml
   server:
     port: {{ .Values.eventhubBackendDeploy.service.port }}
   ```
   3. Используя команду `helm template` проверяем, что шаблоны рендерятся ожидаемым образом. В директории с этим README выполняем команду:
   ```shell
   helm template . --output-dir _rendered --values values.yaml --values postgres.values.yaml --values minio.values.yaml
   ```
   4. Указанная выше команда сложит в папку _rendered манифесты с подставленными значениями
   5. Команда из п.3.3 может упасть, если у вас не скачаны зависимые helm chart-ы. Чтобы их скачать выполните в директории с этим README:
   ```shell
   helm dependency build
   ```
   И повторите шаг 3.3.
4. Если добавляете секрет, необходимо положить его значения для каждого окружения в секреты репозитория.
5. Добавить использование этих секретов в шаги по деплою (файлы `.github/workflows/deploy*`)

## Как добавить переменную среды

Обычно в них скадывают секреты.

1. В `values.yaml` прописываем желаемую иерархию полей (если это секрет в качестве значения используем билиберду). Пример (уже есть) - логин и пароль от почты:
    ```yaml
    eventhubBackendDeploy:
      appConfig:
        mail:
          login: "events.hub@mail.ru"
          # This values must be set up in ci.
          password: "0000" # <- билиберда
    ```
2. В `resources/config.yaml.tpl` прописываем иерархию полей, требуемую приложением, и также указываем, что значение берётся из переменной среды. Пример (уже есть) - логин и пароль от почты:
    ```yaml
    spring:
      mail:
        username: ${EMAIL_LOGIN}
        password: ${MAIL_APPLICATION_PASSWORD}
    ```
3. В `template/deployment.yaml` находим блок `spec.template.spec.containers[0].env` и прописываем туда через `{{ .Values }}` значения полей. Пример (уже есть) - логин и пароль от почты:
   ```yaml
   env:
   - name: EMAIL_LOGIN
     value: {{ .Values.eventhubBackendDeploy.appConfig.mail.login }}
   - name: MAIL_APPLICATION_PASSWORD
     value: {{ .Values.eventhubBackendDeploy.appConfig.mail.password }}
   ```
4. Повторяем шаги 3.3 - 3.5, 4, 5 из предыдущего раздела.
