# Тестовое задание CheckMe

## Установка
В обоих случаях сервер по-умолчанию слушает порт 8080
### Через Docker
`docker-compose up --build -d`
### Без докера
Понадобится PostgreSQL. Будем считать, что он установлен на localhost'е, БД называется checkme,
пользователь так же, а пользовательский пароль password

```shell script
./gradlew shadowJar

export DB_HOST=localhost
export DB_NAME=checkme
export DB_USER=checkme
export DB_PASSWORD=password

java -jar ./build/libs/checkme-test.jar
```

## API
### GET /clinics
Возвращает список клиник
### POST /clinics
```json
{"name": "new clinic"}
```
Создает новую клиник `new clinic`
### PUT /clinics/1
```json
{"name": "renamed clinic"}
```
Переименовывает клинику с id=1
### DELETE /clinics/1
Удаляет клинику с id=1
### POST /clinics/1/check-up
```json
{"id": 1, "price": 100}
```
Добавляет в клинику услугу с id=1 со стоимостью 100
### DELETE /clinics/1/check-up/1
Удаляет из клиники услугу с id=1
### GET /check-ups
Возвращает список услуг
### POST /check-ups
```json
{"name": "new check up"}
```
Создает новую услугу `new check up`
### PUT /check-ups/1
```json
{"name": "renamed check up"}
```
Переименовывает услугу с id=1
### DELETE /check-ups/1
Удаляет услугу с id=1
