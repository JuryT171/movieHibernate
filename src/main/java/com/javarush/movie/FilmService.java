package com.javarush.movie;

import com.javarush.movie.entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.util.List;

public class FilmService {

      //Создаём: фильм + связи с актёрами и категориями + копию в инвентаре магазина 1.
     //Актёров, категории и язык берём существующих (они уже «в актерском составе» базы).

    public static Film createNewFilm(SessionFactory sessionFactory, String title) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Language language = session.createQuery(
                                "select l from Language l where l.name = 'English'", Language.class)
                        .uniqueResult();

                List<Actor> actors = session.createQuery(
                                "select a from Actor a order by rand()", Actor.class)
                        .setMaxResults(3)
                        .list();

                List<Category> categories = session.createQuery(
                                "select c from Category c order by rand()", Category.class)
                        .setMaxResults(2)
                        .list();

                Film film = new Film();
                film.setTitle(title);
                film.setDescription("Совсем свежая новинка, снятая нашим сервисом");
                film.setReleaseYear(2026);
                film.setLanguage(language);
                film.setRentalDuration(5);
                film.setRentalRate(new BigDecimal("4.99"));
                film.setLength(120);
                film.setReplacementCost(new BigDecimal("19.99"));
                film.setRating("PG-13");
                film.setSpecialFeatures("Trailers");
                session.persist(film);
                session.flush(); // сразу получаем film_id — он нужен для составных ключей

                for (Actor actor : actors) {
                    FilmActorId key = new FilmActorId();
                    key.setFilm(film);
                    key.setActor(actor);
                    FilmActor filmActor = new FilmActor();
                    filmActor.setId(key);
                    session.persist(filmActor);
                }

                for (Category category : categories) {
                    FilmCategoryId key = new FilmCategoryId();
                    key.setFilm(film);
                    key.setCategory(category);
                    FilmCategory filmCategory = new FilmCategory();
                    filmCategory.setId(key);
                    session.persist(filmCategory);
                }

                // фильм «доступен для аренды» = есть его копия в магазине
                Store store = session.get(Store.class, (byte) 1);
                Inventory inventory = new Inventory();
                inventory.setFilm(film);
                inventory.setStore(store);
                session.persist(inventory);

                transaction.commit();

                System.out.println("Фильм \"" + film.getTitle() + "\" снят (id=" + film.getFilmId()
                        + "), актёров: " + actors.size()
                        + ", категорий: " + categories.size()
                        + ", копия добавлена в магазин 1");
                return film;
            } catch (Exception e) {
                transaction.rollback(); // откатит фильм, связи и инвентарь разом
                throw new RuntimeException("Не удалось создать фильм", e);
            }
        }
    }
}
