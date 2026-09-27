package com.javarush.movie;

import com.javarush.movie.entity.Address;
import com.javarush.movie.entity.City;
import com.javarush.movie.entity.Customer;
import com.javarush.movie.entity.Store;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

public class CustomerService {

    public static Customer createCustomer(SessionFactory sessionFactory,
                                          String firstName,
                                          String lastName,
                                          String email) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                // магазин, к которому привязываем покупателя
                Store store = session.get(Store.class, (byte) 1);

                // случайный город для нового адреса
                City city = session.createQuery(
                                "select c from City c order by rand()", City.class)
                        .setMaxResults(1)
                        .uniqueResult();

                // создаём адрес покупателя
                Address address = new Address();
                address.setAddress("Random str., " + ThreadLocalRandom.current().nextInt(1, 100));
                address.setDistrict("District " + ThreadLocalRandom.current().nextInt(1, 10));
                address.setCity(city);
                address.setPhone("+1-800-RANDOM");
                session.persist(address);

                // создаём покупателя со ссылкой на этот адрес
                Customer customer = new Customer();
                customer.setStore(store);
                customer.setFirstName(firstName);
                customer.setLastName(lastName);
                customer.setAddress(address);
                customer.setEmail(email);
                customer.setActive(true);
                customer.setCreateDate(LocalDateTime.now()); // у create_date нет дефолта в БД!
                session.persist(customer);

                transaction.commit();
                return customer;
            } catch (Exception e) {
                transaction.rollback(); // откатит И адрес, И покупателя
                throw new RuntimeException("Не удалось создать покупателя", e);
            }
        }
    }
}