package com.javarush.movie.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

// «у этого класса нет собственной жизни:
// он не самостоятельная энтити, а часть другой».
// FilmActorId не мапится на свою таблицу — его содержимое встраивается в FilmActor (владельца, у которого стоит @EmbeddedId).
// В таблице film_actor нет колонок «класса-ключа» — есть только film_id и actor_id, которые ключ предоставляет через свои связи.
@Embeddable
public class FilmActorId implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "film_id")
    private Film film;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "actor_id")
    private Actor actor;

    public FilmActorId() {
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FilmActorId that = (FilmActorId) o;
        return Objects.equals(film, that.film) && Objects.equals(actor, that.actor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(film, actor);
    }
}