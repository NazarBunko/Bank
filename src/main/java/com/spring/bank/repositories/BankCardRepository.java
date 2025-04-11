package com.spring.bank.repositories;

import com.spring.bank.models.BankCard;
import org.hibernate.SessionFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
@Component
public class BankCardRepository {

    private final SessionFactory factory = MainRepository.getFactory(BankCard.class);
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


}
