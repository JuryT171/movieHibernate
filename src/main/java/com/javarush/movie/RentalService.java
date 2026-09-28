package com.javarush.movie;

import com.javarush.movie.entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.time.LocalDateTime;

public class RentalService {

    /**
     * покупатель вернул ранее арендованный фильм.
     * Находим любую открытую аренду (return_date IS NULL) и закрываем её.
     */
    public static void returnFilm(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Rental rental = session.createQuery(
                                "select r from Rental r where r.returnDate is null order by r.rentalId",
                                Rental.class)
                        .setMaxResults(1)
                        .uniqueResult();

                if (rental == null) {
                    throw new IllegalStateException("Открытых аренд нет — возвращать нечего");
                }

                rental.setReturnDate(LocalDateTime.now());

                transaction.commit();

                System.out.println("Аренда id=" + rental.getRentalId() + " закрыта, return_date = "
                        + rental.getReturnDate());
            } catch (Exception e) {
                transaction.rollback();
                throw new RuntimeException("Не удалось вернуть фильм", e);
            }
        }
    }

    /**
     покупатель пришёл в магазин и арендовал фильм.
     * Ограничение: инвентарь должен быть свободен —
     * по нему нет ни одной открытой аренды (return_date is null).
     */
    public static Rental rentFilm(SessionFactory sessionFactory, Short customerId, String filmTitle) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Customer customer = session.get(Customer.class, customerId);
                if (customer == null) {
                    throw new IllegalArgumentException("Покупатель id=" + customerId + " не найден");
                }

                // продавец магазина 1 (в нём же ищем инвентарь)
                Staff staff = session.createQuery(
                                "select s from Staff s where s.store.storeId = 1", Staff.class)
                        .setMaxResults(1)
                        .uniqueResult();

                // свободная копия фильма в магазине 1:
                // не существует открытой аренды по этому инвентарю
                Inventory inventory = session.createQuery("""
                            select i from Inventory i
                            where i.film.title = :title
                              and i.store.storeId = 1
                              and not exists (select r from Rental r
                                              where r.inventory = i
                                                and r.returnDate is null)
                            """, Inventory.class)
                        .setParameter("title", filmTitle)
                        .setMaxResults(1)
                        .uniqueResult();

                if (inventory == null) {
                    throw new IllegalStateException(
                            "Свободных копий фильма \"" + filmTitle + "\" в магазине 1 нет");
                }

                Rental rental = new Rental();
                rental.setRentalDate(LocalDateTime.now());
                rental.setInventory(inventory);
                rental.setCustomer(customer);
                rental.setStaff(staff);
                // returnDate оставляем null — фильм только что взяли
                session.persist(rental);

                Payment payment = new Payment();
                payment.setCustomer(customer);
                payment.setStaff(staff);
                payment.setRental(rental);
                payment.setAmount(inventory.getFilm().getRentalRate());
                payment.setPaymentDate(LocalDateTime.now());
                session.persist(payment);

                // печатаем, пока сессия открыта — ленивые прокси доступны
                System.out.println("Арендован \"" + inventory.getFilm().getTitle()
                        + "\" (инвентарь id=" + inventory.getInventoryId() + ")"
                        + ", оплата $" + payment.getAmount()
                        + ", продавец " + staff.getFirstName() + " " + staff.getLastName());

                transaction.commit();
                return rental;
            } catch (Exception e) {
                transaction.rollback(); // откатит и rental, и payment
                throw new RuntimeException("Не удалось арендовать фильм", e);
            }
        }
    }
}
