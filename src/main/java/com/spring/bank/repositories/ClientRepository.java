package com.spring.bank.repositories;

import com.spring.bank.models.BankAccount;
import com.spring.bank.models.Client;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@Component
public class ClientRepository {

    private final SessionFactory factory = MainRepository.getFactory(Client.class);

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

}
