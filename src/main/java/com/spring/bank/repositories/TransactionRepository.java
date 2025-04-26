package com.spring.bank.repositories;

import com.spring.bank.models.BankCard;
import com.spring.bank.models.PaymentTransaction;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Component
public class TransactionRepository {

    private final SessionFactory factory = MainRepository.getFactory(BankCard.class);

    public List<PaymentTransaction> findTransactionsByCards(List<BankCard> cards) {
        Session session = null;
        try {
            session = factory.getCurrentSession();
            session.beginTransaction();

            // Витягуємо номери карток
            List<String> cardNumbers = cards.stream()
                    .map(BankCard::getCardNumber)
                    .collect(Collectors.toList());

            // HQL-запит для сортування по id транзакції в порядку спадання
            Query<PaymentTransaction> query = session.createQuery(
                    "FROM PaymentTransaction t WHERE t.sender IN :cards OR t.receiver IN :cards ORDER BY t.id DESC",
                    PaymentTransaction.class
            );
            query.setParameter("cards", cardNumbers);

            List<PaymentTransaction> transactions = query.getResultList();

            session.getTransaction().commit();
            System.out.println(transactions);
            return transactions;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public List<Double> getDailyExpensesByCards(List<BankCard> cards) {
        Session session = null;
        try {
            session = factory.getCurrentSession();
            session.beginTransaction();

            List<String> cardNumbers = cards.stream()
                    .map(BankCard::getCardNumber)
                    .collect(Collectors.toList());

            LocalDateTime startDateTime = LocalDate.now().atStartOfDay();
            LocalDateTime endDateTime = LocalDateTime.now();

            Query<PaymentTransaction> query = session.createQuery(
                    "FROM PaymentTransaction t " +
                            "WHERE (t.sender IN :cards) " +
                            "AND t.transactionDate BETWEEN :start AND :end",
                    PaymentTransaction.class
            );
            query.setParameter("cards", cardNumbers);
            query.setParameter("start", startDateTime);
            query.setParameter("end", endDateTime);

            List<PaymentTransaction> transactions = query.getResultList();
            session.getTransaction().commit();

            List<Double> expenses = new ArrayList<>();
            for (LocalDateTime dateTime = startDateTime.toLocalDate().atStartOfDay(); !dateTime.isAfter(endDateTime); dateTime = dateTime.plusDays(1)) {
                expenses.add(0.0);
            }

            for (PaymentTransaction t : transactions) {
                LocalDateTime txDateTime = t.getTransactionDate();
                if (txDateTime.isBefore(startDateTime) || txDateTime.isAfter(endDateTime)) continue;
                int dayIndex = txDateTime.getDayOfMonth() - 1;
                expenses.set(dayIndex, expenses.get(dayIndex) + t.getAmount());
            }

            return expenses;

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return Collections.emptyList();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public List<Double> getDailyDepositsByCards(List<BankCard> cards) {
        Session session = null;
        try {
            session = factory.getCurrentSession();
            session.beginTransaction();

            List<String> cardNumbers = cards.stream()
                    .map(BankCard::getCardNumber)
                    .collect(Collectors.toList());

            LocalDateTime startDateTime = LocalDate.now().atStartOfDay();
            LocalDateTime endDateTime = LocalDateTime.now();

            Query<PaymentTransaction> query = session.createQuery(
                    "FROM PaymentTransaction t " +
                            "WHERE (t.receiver IN :cards) " +
                            "AND t.transactionDate BETWEEN :start AND :end",
                    PaymentTransaction.class
            );
            query.setParameter("cards", cardNumbers);
            query.setParameter("start", startDateTime);
            query.setParameter("end", endDateTime);

            List<PaymentTransaction> transactions = query.getResultList();
            session.getTransaction().commit();

            List<Double> deposits = new ArrayList<>();
            for (LocalDateTime dateTime = startDateTime.toLocalDate().atStartOfDay(); !dateTime.isAfter(endDateTime); dateTime = dateTime.plusDays(1)) {
                deposits.add(0.0);
            }

            for (PaymentTransaction t : transactions) {
                LocalDateTime txDateTime = t.getTransactionDate();
                if (txDateTime.isBefore(startDateTime) || txDateTime.isAfter(endDateTime)) continue;
                int dayIndex = txDateTime.getDayOfMonth() - 1;
                deposits.set(dayIndex, deposits.get(dayIndex) + t.getAmount());
            }

            return deposits;

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return Collections.emptyList();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
}
