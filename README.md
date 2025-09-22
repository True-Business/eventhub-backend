# eventhub-backend
Бэкенд сервиса 

## Запуск с помощью docker compose

```shell
docker compose up --build -d
```

## Запуск с помощью helm

Скачать необходимые чарты

```shell
helm dependency build ./helm/eventhub-backend/
```

Задеплоить в кластер

```shell
helm upgrade eventhub-backend \
    --install \
    --namespace eventhub-backend \
    --create-namespace \
    ./helm/eventhub-backend/ \
    --reuse-values \
    -f ./helm/eventhub-backend/values.yaml \
    -f ./helm/eventhub-backend/postgres.values.yaml \
    --kubeconfig PATH_TO_YOUR_KUBECONFIG_FILE
```
