package com.javarush.movie.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "address", schema = "movie")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Short addressId;

    @Column(name = "address", nullable = false, length = 50)
    private String address;

    @Column(name = "address2", length = 50)
    private String address2;

    @Column(name = "district", nullable = false, length = 20)
    private String district;

    // много адрессов могут ссылаться на один город
    @ManyToOne(fetch = FetchType.LAZY, optional = false) // город не загружается сразу с адресом. Вместо объекта Hibernate подкладывает прокси.
    @JoinColumn(name = "city_id") // имя колонки-внешнего ключа в таблице address
    private City city;

    @Column(name = "postal_code", length = 10)
    private String postalCode;

    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @Column(name = "last_update", insertable = false, updatable = false)
    private LocalDateTime lastUpdate;



    public void setAddress(String address) {
        this.address = address;
    }



    public void setDistrict(String district) {
        this.district = district;
    }



    public void setCity(City city) {
        this.city = city;
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
