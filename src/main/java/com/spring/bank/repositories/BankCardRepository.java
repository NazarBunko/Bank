package com.spring.bank.repositories;

import com.spring.bank.models.BankCard;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
@Component
public class BankCardRepository {

    private final SessionFactory factory = MainRepository.getFactory(BankCard.class);
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public List<BankCard> findByBankAccountId(Integer bankAccountId) {
        try (Session session = factory.openSession()) {
            return session.createQuery("FROM BankCard WHERE bankAccountId = :bankAccountId", BankCard.class)
                    .setParameter("bankAccountId", bankAccountId)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public void addCard(BankCard card) {
        System.out.println("3");
        System.out.println(card.toString());
        Transaction transaction = null;
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            session.save(card);
            transaction.commit();
            System.out.println("4");
        } catch (Exception e) {
            System.out.println("5");
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
}
