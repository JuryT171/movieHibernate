package com.javarush.movie.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "film_actor", schema = "movie")
public class FilmActor {

    @EmbeddedId
    private FilmActorId id;

    @Column(name = "last_update", insertable = false, updatable = false)
    private LocalDateTime lastUpdate;



    public FilmActorId getId() {
        return id;
    }

    public void setId(FilmActorId id) {
        this.id = id;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
