# Учебный бот УрФУ @sup_urfu_bot
___
### Описание:
Бот написан с целью помогать студентам Уральского федерального университета.
Он работает на платформе - Telegram и Discord, также он использует базу данных MySQL для хранения данных пользователей и api УрФУ для получения расписания.
Код бота написан на языке Java, в качестве учебного проекта.
### Возможности бота:
* Сохранять расписание для конкретной группы с официального сайта УрФУ
* Выдавать расписание на определённый день для каждого пользователя
* Редактировать и сохранять личное расписание, введённое пользователем
* Узнавать следующую пару на текущий момент
* Сохранять дедлайны
* Уведомлять о приближающихся дедлайнах и парах
___
### Пример работы:

![1](https://github.com/syoumzic/java-bot/assets/114348208/b2f042aa-c248-4c9c-a3a9-5cc4dddb7b43)
![2](https://github.com/syoumzic/java-bot/assets/114348208/8f3c3ef0-e993-4bce-8a35-b365547dc3e0)
![3](https://github.com/syoumzic/java-bot/assets/114348208/50dc5fe2-0794-4d45-bcfa-014b5c636863)
![4](https://github.com/syoumzic/java-bot/assets/114348208/0122a75b-385a-4b97-9de2-5eeca945edc4)

### Заметка для запуска бота
Необходимо инициализирвать файл .env:
* TG_TOKEN='токен телеграм бота'
* DS_TOKEN='токен дискорд бота'
* URL='Ссылка для подключения к MySQL'
* NAMEUSER='имя для бд'
* MYSQL_ROOT_PASSWORD='пароль для бд'
### Инструкция по сборке проекта на сервере
* Клонируем репозиторий;
* Создаём в корневой папке проекта файл .env(предыдущий пункт);
* Создаём локальную сеть для контейнера:
  #### sudo docker network create botnet
* Создаём пространство для базы данных:
  #### sudo docker volume create database 
* Собираем проект:
  #### sudo docker-compose -p bot up -d
* Настраиваем MYSQL:
  #### sudo docker exec -it db mysql -u root -p 
* Вводим пароль для доступа к базе данных (по умолчанию root);
* Создаём базу данных выполняя SQL запрос:
  #### create database `mydbtest`;
* Выбираем базу данных:
  #### use `mydbtest`;
* Создаём главную таблицу с пользователями:
  #### create table \`users\` (\`id\` varchar(45) not null primary key, \`group\` varchar(20) not null, \`useIndiv\` tinyint not null default 1, \`time\` int not null default 10, \`notification\` tinyint not null default 0, \`existDL\` tinyint not null default 0, \`deadline_shift\` int not null default 1);
#### При возникновении ошибки ERROR [internal] booting buildkit сборку выполнить командой: sudo DOCKER_BUILDKIT=0 docker-compose -p bot up -d
