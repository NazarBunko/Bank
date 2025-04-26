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

    public List<Double> getDailyTransactionSumsByCards(List<BankCard> cards, boolean isExpense) {
        Session session = null;
        try {
            session = factory.getCurrentSession();
            session.beginTransaction();

            List<String> cardNumbers = cards.stream()
                    .map(BankCard::getCardNumber)
                    .collect(Collectors.toList());

            LocalDate now = LocalDate.now();
            LocalDate firstDayOfMonth = now.withDayOfMonth(1);

            String field = isExpense ? "t.sender" : "t.receiver";

            Query<Object[]> query = session.createQuery(
                    "SELECT DAY(t.transactionDate), SUM(t.amount) " +
                            "FROM PaymentTransaction t " +
                            "WHERE " + field + " IN :cards " +
                            "AND t.transactionDate >= :startDate " +
                            "AND t.transactionDate <= :endDate " +
                            "GROUP BY DAY(t.transactionDate)",
                    Object[].class
            );
            query.setParameter("cards", cardNumbers);
            query.setParameter("startDate", firstDayOfMonth.atStartOfDay());
            query.setParameter("endDate", now.atTime(23, 59, 59));

            List<Object[]> results = query.getResultList();
            session.getTransaction().commit();

            List<Double> dailySums = new ArrayList<>(Collections.nCopies(now.getDayOfMonth(), 0.0));

            for (Object[] row : results) {
                Integer day = (Integer) row[0];
                Double sum = (Double) row[1];
                if (day != null && day >= 1 && day <= now.getDayOfMonth()) {
                    dailySums.set(day - 1, sum != null ? sum : 0.0);
                }
            }

            return dailySums;

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
