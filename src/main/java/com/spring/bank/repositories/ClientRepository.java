package com.spring.bank.repositories;

import com.spring.bank.models.BankAccount;
import com.spring.bank.models.Client;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
@Component
public class ClientRepository {

    private final SessionFactory factory = MainRepository.getFactory(Client.class);
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public List<Client> getAllClients(){
        Session session = null;
        try {
            session = factory.getCurrentSession();
            session.beginTransaction();
            List<Client> clients = session.createQuery("from Client").list();
            session.getTransaction().commit();
            return clients;
        } catch (Exception e) {
            System.out.println("Error '" + e.getMessage() + "' in method getAllClients()");
            return null;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public Client getClientById(int id){
        Session session = null;
        try {
            session = factory.getCurrentSession();
            session.beginTransaction();
            Client client = session.get(Client.class, id);
            session.getTransaction().commit();
            return client;
        } catch (Exception e) {
            System.out.println("Error '" + e.getMessage() + "' in method getClientById(int id)");
            return null;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public boolean addClient(Client client) {
        Session session = null;
        try {
            session = factory.getCurrentSession();
            session.beginTransaction();

            session.save(client);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            System.out.println("Error '" + e.getMessage() + "' in method addClient()");
            return false;
        } finally {
            if (session != null && session.isOpen()) session.close();
        }
    }

    public Client getLastAddedClient() {
        Session session = null;
        try {
            session = factory.getCurrentSession();
            session.beginTransaction();

            Client client = (Client) session.createQuery("FROM Client ORDER BY id DESC")
                    .setMaxResults(1)
                    .uniqueResult();

            session.getTransaction().commit();
            return client;
        } catch (Exception e) {
            System.out.println("Error '" + e.getMessage() + "' in method getLastAddedClient()");
            return null;
        } finally {
            if (session != null && session.isOpen()) session.close();
        }
    }

    public boolean updateClientName(Client client, String password, String newName, BankAccount bankAccount) {

        if (!passwordEncoder.matches(password, bankAccount.getPassword())) {
            return false;
        }

        Session session = null;
        try {
            session = factory.getCurrentSession();
            session.beginTransaction();

            client.setFullName(newName);
            session.update(client);

            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            return false;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public boolean updateClientAddress(Client client, String password, String address, BankAccount bankAccount) {

        if (!passwordEncoder.matches(password, bankAccount.getPassword())) {
            return false;
        }

        Session session = null;
        try {
            session = factory.getCurrentSession();
            session.beginTransaction();

            client.setAddress(address);
            session.update(client);

            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            return false;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public boolean updateClientBirthDate(Client client, String password, LocalDate date, BankAccount bankAccount) {

        if (!passwordEncoder.matches(password, bankAccount.getPassword())) {
            return false;
        }

        Session session = null;
        try {
            session = factory.getCurrentSession();
            session.beginTransaction();

            client.setBirthDate(date);
            session.update(client);

            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            return false;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public boolean updateClientType(Client client, String password, String type, BankAccount bankAccount) {

        if (!passwordEncoder.matches(password, bankAccount.getPassword())) {
            return false;
        }

        Session session = null;
        try {
            session = factory.getCurrentSession();
            session.beginTransaction();

            client.setClientType(type);
            session.update(client);

            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            return false;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public boolean updateClientPassport(Client client, String password, String passport, BankAccount bankAccount) {
        if (!passwordEncoder.matches(password, bankAccount.getPassword())) {
            return false;
        }

        Client existingClient = null;
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();

            existingClient = session.createQuery("FROM Client WHERE passportNumber = :passport", Client.class)
                    .setParameter("passport", passport)
                    .uniqueResult();

            if (existingClient != null) {
                session.getTransaction().commit();
                return false;
            }

            client.setPassportNumber(passport);
            session.update(client);

            session.getTransaction().commit();
            return true;

        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            return false;
        }
    }

    public boolean updateClientPhone(Client client, String password, String phone, BankAccount bankAccount) {

        if (!passwordEncoder.matches(password, bankAccount.getPassword())) {
            return false;
        }

        Session session = null;
        try {
            session = factory.getCurrentSession();
            session.beginTransaction();

            client.setPhone(phone);
            session.update(client);

            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            return false;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

}
