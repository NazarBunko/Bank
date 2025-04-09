package com.spring.bank.repositories;

import com.spring.bank.models.Client;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
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
}
