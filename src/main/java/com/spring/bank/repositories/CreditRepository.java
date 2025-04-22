package com.spring.bank.repositories;

import com.spring.bank.models.BankCard;
import com.spring.bank.models.Client;
import com.spring.bank.models.Credit;
import com.spring.bank.models.PaymentTransaction;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;

@Repository
@Component
public class CreditRepository {
    private final SessionFactory factory = MainRepository.getFactory(Credit.class);

    public List<Credit> findAllByClientId(Integer clientId) {
        try (Session session = factory.openSession()) {
            return session.createQuery(
                            "FROM Credit WHERE clientId = :clientId AND status = 'Відкритий'", Credit.class)
                    .setParameter("clientId", clientId)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public boolean repayCredit(int creditId, double amount) {
        Transaction transaction;

        try (Session session = factory.openSession()) {
            Credit credit = session.get(Credit.class, creditId);
            if (credit == null || amount > credit.getAmount()) {
                return false;
            }

            double oldAmount = credit.getAmount();
            double newAmount = oldAmount - amount;

            transaction = session.beginTransaction();

            if (newAmount <= 0.01) {
                credit.setAmount(0.0);
                credit.setStatus("Закритий");
            } else {
                credit.setAmount(newAmount);
            }

            session.update(credit);
            transaction.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
