# eventhub-backend
Бэкенд сервиса 

## Запуск с помощью docker compose

```
docker compose up --build -d
```

## Запуск с помощью helm

Скачать необходимые чарты

```
helm dependency build ./helm/eventhub-backend/
```

Задеплоить в кластер

```
helm upgrade \
    --install \
    --namespace eventhub-backend \
    --create-namespace eventhub-backend \
    ./helm/eventhub-backend/ \
    --reuse-values \
    -f ./helm/eventhub-backend/values.yaml \
    -f ./helm/eventhub-backend/postgres.values.yaml \
    --kubeconfig PATH_TO_YOUR_KUBECONFIG_FILE
```