package com.javarush.movie;

import com.javarush.movie.entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Main {
    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        configuration.configure();
        configuration.addAnnotatedClass(Language.class);
        configuration.addAnnotatedClass(Film.class);
        configuration.addAnnotatedClass(Inventory.class);
        configuration.addAnnotatedClass(Rental.class);
        configuration.addAnnotatedClass(Country.class);
        configuration.addAnnotatedClass(City.class);
        configuration.addAnnotatedClass(Address.class);
        configuration.addAnnotatedClass(Store.class);
        configuration.addAnnotatedClass(Staff.class);
        configuration.addAnnotatedClass(Customer.class);

        try (SessionFactory sessionFactory = configuration.buildSessionFactory()) {

            Customer customer = CustomerService.createCustomer(
                    sessionFactory, "Иван", "Иванов", "ivanov@example.com");

            com.javarush.movie.RentalService.returnFilm(sessionFactory);

            System.out.println("Создан покупатель id=" + customer.getCustomerId()
                    + " (" + customer.getFirstName() + " " + customer.getLastName() + ")");
        }
    }
}
