package com.javarush.movie;

import com.javarush.movie.entity.Rental;
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
}
