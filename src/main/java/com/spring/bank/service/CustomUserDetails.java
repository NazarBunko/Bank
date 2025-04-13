package com.spring.bank.service;

import com.spring.bank.models.BankAccount;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {
    private final BankAccount account;

    public CustomUserDetails(BankAccount account) {
        this.account = account;
    }

    public BankAccount getAccount() {
        return account;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return account.getPassword(); // або hashedPassword, якщо є
    }

    @Override
    public String getUsername() {
        return account.getLogin(); // або email, якщо логін — це email
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}

