package com.javarush.movie;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Main {
    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        configuration.configure();

        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {

            Object count = session.createNativeQuery("select count(*) from film").uniqueResult();
            System.out.println("Фильмов в базе: " + count);
        }
    }
}
