package com.spring.bank.repositories;

import com.spring.bank.models.BankCard;
import com.spring.bank.models.PaymentTransaction;
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
        Transaction transaction = null;
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            session.save(card);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public boolean replenishCard(String cardNumber, Double amount, String method) {
        try (Session session = factory.openSession()) {
            BankCard card = findCardByNumber(cardNumber);

            if (card == null) {
                return false;
            }

            card.setBalance(card.getBalance() + amount);
            Transaction transaction = session.beginTransaction();
            session.update(card);

            transaction.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return false;
        }
    }


    public BankCard findCardByNumber(String cardNumber) {
        try (Session session = factory.openSession()) {
            return session.createQuery("FROM BankCard WHERE cardNumber = :cardNumber", BankCard.class)
                    .setParameter("cardNumber", cardNumber)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean transfer(String fromCardNumber, String toCardNumber, Double amount, String description) {
        Transaction transaction;

        try (Session session = factory.openSession()) {
            BankCard senderCard = findCardByNumber(fromCardNumber);
            BankCard receiverCard = findCardByNumber(toCardNumber);
            if (senderCard == null || receiverCard == null) {
                System.out.println("Одна з карток не знайдена");
                return false;
            }
            if (senderCard.getBalance() < amount) {
                System.out.println("Недостатньо коштів");
                return false;
            }
            transaction = session.beginTransaction();
            senderCard.setBalance(senderCard.getBalance() - amount);
            receiverCard.setBalance(receiverCard.getBalance() + amount);
            session.update(senderCard);
            session.update(receiverCard);
            transaction.commit();
            boolean newTransaction = createTransfer(fromCardNumber, toCardNumber, amount, description);
            if (newTransaction) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean createTransfer(String fromCardNumber, String toCardNumber, Double amount, String description) {
        Transaction transaction;

        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            PaymentTransaction newOperation = new PaymentTransaction(fromCardNumber, toCardNumber, amount, description);
            session.save(newOperation);
            transaction.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
