package com.spring.bank.controllers;

import com.spring.bank.models.BankAccount;
import com.spring.bank.models.Client;
import com.spring.bank.repositories.BankAccountRepository;
import com.spring.bank.repositories.ClientRepository;
import com.spring.bank.service.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/menu")
public class ClientController {

    private ClientRepository clientRepository;
    private BankAccountRepository bankAccountRepository;

    public ClientController() {
        clientRepository = new ClientRepository();
        bankAccountRepository = new BankAccountRepository();
    }

    public BankAccount getBankAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            BankAccount account = userDetails.getAccount();
            return account;
        } else {
            return null;
        }
    }

    public boolean checkRole() {
        return "ROLE_ADMIN".equals(getBankAccount().getRole());
    }

    @GetMapping("/allClients")
    public List<Client> getAllClients() {
        if(checkRole()){
            return clientRepository.getAllClients();
        } else {
            return null;
        }
    }

    @GetMapping("/client/{id}")
    public Client getClient(@PathVariable int id) {
        if(checkRole()){
            return clientRepository.getClientById(id);
        } else {
            return null;
        }
    }

    @GetMapping("/client/me")
    public Client getMyClient() {
        return clientRepository.getClientById(getBankAccount().getClientId());
    }

    @GetMapping("/allAccounts")
    public List<BankAccount> getAllBankAccounts() {
        if(checkRole()){
            return bankAccountRepository.getAllAccounts();
        } else {
            return null;
        }
    }

    @GetMapping("/account/{id}")
    public BankAccount getBankAccount(@PathVariable int id) {
        if(checkRole()){
            return bankAccountRepository.getAccountById(id);
        } else {
            return null;
        }
    }

    @GetMapping("/account/me")
    public BankAccount getMyBankAccount() {
        return getBankAccount();
    }
}
