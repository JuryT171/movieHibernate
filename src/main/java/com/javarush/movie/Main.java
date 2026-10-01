package com.javarush.movie;

import com.javarush.movie.entity.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Main {
    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        configuration.configure(); // метод для настроек cfg.xml
        configuration.addAnnotatedClass(FilmText.class);
        // Регистрация энтити. Без этого Hibernate не знает о существовании класса
        configuration.addAnnotatedClass(Actor.class);
        configuration.addAnnotatedClass(Category.class);
        configuration.addAnnotatedClass(FilmActor.class);
        configuration.addAnnotatedClass(FilmCategory.class);
        configuration.addAnnotatedClass(Payment.class);
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
        // создаем сессию
        try (SessionFactory sessionFactory = configuration.buildSessionFactory()) {
            // создаем покупателя
            Customer customer = CustomerService.createCustomer(
                    sessionFactory, "Иван", "Иванов", "ivanov@example.com");
            System.out.println("Создан покупатель id=" + customer.getCustomerId()
                    + " (" + customer.getFirstName() + " " + customer.getLastName() + ")");

            // аренда фильма
            RentalService.returnFilm(sessionFactory);
            Rental rental = RentalService.rentFilm(sessionFactory, customer.getCustomerId(), "AFFAIR PREJUDICE");
            System.out.println("Новая аренда id=" + rental.getRentalId());

            Film newFilm = FilmService.createNewFilm(sessionFactory, "JAVA RUSH: THE MOVIE");
            //  сразу арендуем снятый фильм
            RentalService.rentFilm(sessionFactory, customer.getCustomerId(), newFilm.getTitle());
        }
    }
}
