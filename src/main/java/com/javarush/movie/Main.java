package com.javarush.movie;

import com.javarush.movie.entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Main {
    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        configuration.configure();
        configuration.addAnnotatedClass(Country.class);
        configuration.addAnnotatedClass(City.class);
        configuration.addAnnotatedClass(Address.class);
        configuration.addAnnotatedClass(Store.class);
        configuration.addAnnotatedClass(Staff.class);
        configuration.addAnnotatedClass(Customer.class);

        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {

            Store store = session.get(Store.class, (byte) 1);
            System.out.println("Адрес магазина: " + store.getAddress().getAddress());
            System.out.println("Страна: " + store.getAddress().getCity().getCountry().getCountry());

            Long customers = session.createQuery("select count(c) from Customer c", Long.class).uniqueResult();
            System.out.println("Покупателей: " + customers);
        }
    }
}
