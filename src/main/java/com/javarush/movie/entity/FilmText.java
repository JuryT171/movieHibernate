package com.javarush.movie.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "film_text", schema = "movie")
public class FilmText {

    @Id
    @Column(name = "film_id")
    private Short filmId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // PK этой энтити берётся из связи с Film
    // на join-колонке — обязательная пара с @MapsId, иначе Hibernate попытается записать film_id дважды (из id-поля и из связи) и упадёт с ошибкой маппинга.
    @JoinColumn(name = "film_id", insertable = false, updatable = false)
    private Film film;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "text")
    private String description;

    @Column(name = "last_update", insertable = false, updatable = false)
    private LocalDateTime lastUpdate;


    public Short getFilmId() {
        return filmId;
    }

    public void setFilmId(Short filmId) {
        this.filmId = filmId;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
