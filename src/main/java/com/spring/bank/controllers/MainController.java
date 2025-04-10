package com.spring.bank.controllers;

import com.spring.bank.models.BankAccount;
import com.spring.bank.models.Client;
import com.spring.bank.repositories.BankAccountRepository;
import com.spring.bank.repositories.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@RestController
@AllArgsConstructor
@RequestMapping("/app")
public class MainController {
    private ClientRepository clientRepository;
    private BankAccountRepository bankAccountRepository;

    public MainController() {
        clientRepository = new ClientRepository();
        bankAccountRepository = new BankAccountRepository();
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to the bank";
    }

    @GetMapping("/add")
    public String addClient(/*@RequestParam String fullName,
                            @RequestParam java.time.LocalDate birthDate,
                            @RequestParam String passwordNumber,
                            @RequestParam String address,
                            @RequestParam String phone,
                            @RequestParam String clientType,
                            @RequestParam String password,
                            @RequestParam String accountType*/) {
        String fullName = "Дмитро Ковальчук";
        String birthDate = "2000-02-10";
        String passwordNumber = "AB123459";
        String address = "вул. Свободи, 17";
        String phone = "+380976949801";
        String clientType = "звичайний";
        String password = "123456";
        String accountType = "депозитний";
        try{
            Client client = new Client(fullName, birthDate, passwordNumber, address, phone, clientType);
            clientRepository.addClient(client);
            client = clientRepository.getLastAddedClient();
            BankAccount bankAccount = new BankAccount(client.getId(), password, accountType);
            bankAccountRepository.addAccount(bankAccount);
            System.out.println("Client added");
            return "Client added successfully";
        }
        catch(Exception e){
            System.out.println("Error: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }
}
