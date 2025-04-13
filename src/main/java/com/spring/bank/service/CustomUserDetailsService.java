package com.spring.bank.service;

import com.spring.bank.models.BankAccount;
import com.spring.bank.repositories.BankAccountRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final BankAccountRepository bankAccountRepository;

    public CustomUserDetailsService(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        BankAccount account = bankAccountRepository.findByLogin(username);
        if (account == null) {
            throw new UsernameNotFoundException("Користувача не знайдено: " + username);
        }
        return new CustomUserDetails(account);
    }
}

