## Телеграм бот

### Настройка RabbitMQ
Скачать образ rabbitmq: <br />
\$ docker pull rabbitmq:3.11.0-management <br />

Создать volume: <br />
\$ docker volume create rabbitmq_data <br />

Запустить контейнер с rabbitmq: <br />
\$ docker run -d --hostname rabbitmq --name rabbitmq -p 5672:5672 -p 15672:15672 -v rabbitmq_data:/var/lib/docker/volumes/rabbitmq_data/_data --restart=unless-stopped rabbitmq:3.11.0-management <br />
Флаги: <br />
--detach , -d запустит контейнер в фоновом режиме и вернет идентификатор контейнера в терминал; <br />
--hostname адрес контейнера для подключения к нему внутри docker из других контейнеров; <br />
--name имя контейнера; <br />
-p порты: контейнер RabbitMQ, веб-интерфейс (внешний:внутренний); <br />
-v примонтировать volume (том) - внешнее хранилище данных, /var/lib/docker/volumes/rabbitmq_data/_data - путь к тому; <br />
--restart=unless-stopped контейнер будет подниматься заново при каждом перезапуске системы (точнее, при запуске docker); <br />

Команда для определения пути к тому хранилища: <br />
\$ docker volume inspect rabbitmq_data <br />
поле Mountpoint из представленного json - адрес хранилища в системе <br />

Подключиться к контейнеру с rabbitmq: <br />
\$ docker exec -it rabbitmq /bin/bash <br />

Внутри контейнера создать пользователя, сделать его админом и установить права: <br />
\$ rabbitmqctl add_user userok p@ssw0rd <br />
\$ rabbitmqctl set_user_tags userok administrator <br />
\$ rabbitmqctl set_permissions -p / userok ".*" ".*" ".*" <br />
