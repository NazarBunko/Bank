package com.spring.bank.repositories;

import com.spring.bank.models.BankAccount;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Component
public class BankAccountRepository {

    private final SessionFactory factory = MainRepository.getFactory(BankAccount.class);
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public List<BankAccount> getAllAccounts() {
        Session session = null;
        try {
            session = factory.getCurrentSession();
            session.beginTransaction();
            List<BankAccount> accounts = session.createQuery("from BankAccount", BankAccount.class).list();
            session.getTransaction().commit();
            return accounts;
        } catch (Exception e) {
            System.out.println("Error '" + e.getMessage() + "' in method getAllAccounts()");
            return null;
        } finally {
            if (session != null && session.isOpen()) session.close();
        }
    }

    public BankAccount getAccountById(int id) {
        Session session = null;
        try {
            session = factory.getCurrentSession();
            session.beginTransaction();
            BankAccount account = session.get(BankAccount.class, id);
            session.getTransaction().commit();
            return account;
        } catch (Exception e) {
            System.out.println("Error '" + e.getMessage() + "' in method getAccountById()");
            return null;
        } finally {
            if (session != null && session.isOpen()) session.close();
        }
    }

    public boolean addAccount(BankAccount account) {
        Session session = null;
        try {
            session = factory.getCurrentSession();
            session.beginTransaction();

            String rawPassword = account.getPassword();
            String encodedPassword = passwordEncoder.encode(rawPassword);
            account.setPassword(encodedPassword);

            session.save(account);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            System.out.println("Error '" + e.getMessage() + "' in method addAccount()");
            return false;
        } finally {
            if (session != null && session.isOpen()) session.close();
        }
    }
}
