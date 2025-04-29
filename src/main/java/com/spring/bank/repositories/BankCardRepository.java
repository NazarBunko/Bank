package com.spring.bank.repositories;

import com.spring.bank.models.BankAccount;
import com.spring.bank.models.BankCard;
import com.spring.bank.models.Credit;
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

            boolean newTransaction = createTransfer(method, cardNumber, amount, "replenish");
            if (newTransaction) {
                return true;
            } else {
                return false;
            }
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

    public boolean mobile(String fromCardNumber, String phone, Double amount) {
        Transaction transaction;

        try (Session session = factory.openSession()) {
            BankCard senderCard = findCardByNumber(fromCardNumber);
            if (senderCard == null) {
                System.out.println("Одна з карток не знайдена");
                return false;
            }
            if (senderCard.getBalance() < amount) {
                System.out.println("Недостатньо коштів");
                return false;
            }
            transaction = session.beginTransaction();
            senderCard.setBalance(senderCard.getBalance() - amount);
            session.update(senderCard);
            transaction.commit();
            boolean newTransaction = createTransfer(fromCardNumber, phone, amount, "mobile top-up");
            if (newTransaction) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public double credit(String cardNumber, Double amount, double InterestRate, Integer TermMonths, BankAccount bankAccount) {
        Transaction transaction;

        Integer clientId = bankAccount.getClientId();
        if (clientId == null) {
            return 0;
        }

        double monthlyRate = InterestRate / 100.0 / 12.0;
        double monthlyPayment = (amount * monthlyRate) /
                (1.0 - Math.pow(1.0 + monthlyRate, -TermMonths));

        monthlyPayment = Math.round(monthlyPayment * 100.0) / 100.0;
        double totalPayment = monthlyPayment * TermMonths;
        totalPayment = Math.round(totalPayment * 100.0) / 100.0;


        Credit credit = new Credit();
        credit.setClientId(clientId);
        credit.setBankAccountId(bankAccount.getId());
        credit.setCardNumber(cardNumber);
        credit.setPrincipalAmount(totalPayment);
        credit.setAmount(totalPayment);
        credit.setInterestRate(InterestRate);
        credit.setTermMonths(TermMonths);
        credit.setMonthlyPayment(monthlyPayment);
        credit.setStatus("Відкритий");

        try (Session session = factory.openSession()) {
            BankCard receiverCard = findCardByNumber(cardNumber);
            if (receiverCard == null) {
                System.out.println("Картка не знайдена");
                return 0;
            }
            if(credit == null) {
                System.out.println("Кредит не знайдений");
            }
            transaction = session.beginTransaction();
            receiverCard.setBalance(receiverCard.getBalance() + amount);
            session.update(receiverCard);
            session.save(credit);
            transaction.commit();
            boolean newTransaction = createTransfer("bank", cardNumber, amount, "credit");
            if (newTransaction) {
                return monthlyPayment;
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public boolean repayCredit(double amount, String cardNumber) {
        Transaction transaction;

        try (Session session = factory.openSession()) {
            BankCard card = findCardByNumber(cardNumber);
            transaction = session.beginTransaction();
            card.setBalance(card.getBalance() - amount);
            session.update(card);
            transaction.commit();
            boolean newTransaction = createTransfer(cardNumber, "bank", amount, "credit repay");
            if (newTransaction) {
                return true;
            } else {
                return false;
            }
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
