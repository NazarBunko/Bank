package com.spring.bank.controllers;

import com.spring.bank.models.BankAccount;
import com.spring.bank.models.Client;
import com.spring.bank.repositories.BankAccountRepository;
import com.spring.bank.repositories.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
public class MainController {
    private ClientRepository clientRepository;
    private BankAccountRepository bankAccountRepository;

    public MainController() {
        clientRepository = new ClientRepository();
        bankAccountRepository = new BankAccountRepository();
    }

    @GetMapping("/")
    public String welcome() {
        return "welcome";
    }

    @GetMapping("/signIn")
    public String signIn() {
        return "registrationForm";
    }

    @GetMapping("/logIn")
    public String logIn() {
        return "loginForm";
    }

    @PostMapping("/signIn")
    public String addClient(@RequestParam String fullName,
                            @RequestParam java.time.LocalDate birthDate,
                            @RequestParam String passwordNumber,
                            @RequestParam String address,
                            @RequestParam String phone,
                            @RequestParam String clientType,
                            @RequestParam String login,
                            @RequestParam String password,
                            @RequestParam String accountType) {
        try{
            Client client = new Client(fullName, birthDate, passwordNumber, address, phone, clientType);
            clientRepository.addClient(client);
            client = clientRepository.getLastAddedClient();
            BankAccount bankAccount = new BankAccount(client.getId(), login, password, accountType);
            bankAccountRepository.addAccount(bankAccount);
            return "redirect:/success";
        }
        catch(Exception e){
            return "redirect:/failed";
        }
    }

    @GetMapping("/success")
    public String successfully() {
        return "successfully";
    }

    @GetMapping("/failed")
    public String failed() {
        return "failed";
    }

}
