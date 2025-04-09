package com.spring.bank.repositories;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class MainRepository {

    public static <T> SessionFactory getFactory(Class<T> clazz) {
        return new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(clazz)
                .buildSessionFactory();
    }
}
