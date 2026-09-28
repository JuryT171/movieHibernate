Movie Hibernate

Учебный проект (JavaRush, Hibernate Module 2): маппинг Entity-классов на существующуюсхему БД movie (зафиксированный дамп тестовой БД Sakila) и транзакционныебизнес-сценарии для проверки корректности маппинга.

Технологии

Библиотека	Версия	Назначение
Java	17	платформа
Hibernate ORM (jakarta)	5.6.15.Final	ORM, маппинг энтити
MySQL Connector/J	8.0.33	JDBC-драйвер
p6spy	3.9.1	логирование реальных SQL-запросов с параметрами
Maven	—	сборка


Подготовка окружения

Установить MySQL Server 8.x, задать пароль пользователя root.
Развернуть дамп: mysql -u root -p < movie.sql(дамп не содержит CREATE DATABASE, поэтому сначала выполнить CREATE DATABASE movie;и убедиться, что схема по умолчанию — именно movie).
Вписать пароль БД в src/main/resources/hibernate.cfg.xml.
Собрать и запустить: mvn compile exec:java -Dexec.mainClass="com.javarush.movie.Main"или кнопку Run у класса Main в IDEA.
Структура проекта

src/main/java/com/javarush/movie├── Main                 # сборка SessionFactory, запуск сценариев├── CustomerService      # п.1: создание покупателя├── RentalService        # п.2: возврат фильма; п.3: аренда├── FilmService          # п.4: «сняли новый фильм»└── entity               # 16 Entity-классов на все таблицы схемы movie    ├── ...              # (+ 2 @Embeddable-класса составных ключей)
Ключевые решения маппинга:

last_update у всех таблиц — insertable = false, updatable = false:колонку заполняет сама БД (DEFAULT CURRENT_TIMESTAMP ON UPDATE ...).
film → language — @ManyToOne (у фильма ровно один язык),original_language_id замаплен опционально (во всех данных NULL).
film_actor, film_category — отдельные энтити с @EmbeddedId,т.к. первичный ключ составной.
film_text — @OneToOne + @MapsId: film_id одновременно PK и ссылка на film.
Связи по умолчанию FetchType.LAZY.

