package com.spring.bank.controllers;

import com.spring.bank.models.BankAccount;
import com.spring.bank.models.Client;
import com.spring.bank.repositories.BankAccountRepository;
import com.spring.bank.repositories.BankCardRepository;
import com.spring.bank.repositories.ClientRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class MainController {
    private ClientRepository clientRepository;
    private BankAccountRepository bankAccountRepository;
    private BankCardRepository bankCardRepository;

    public MainController() {
        clientRepository = new ClientRepository();
        bankAccountRepository = new BankAccountRepository();
        bankCardRepository = new BankCardRepository();
    }

    @GetMapping("/")
    public String welcome() {
        return "welcome";
    }

    @GetMapping("/logIn")
    public String logIn() {
        return "loginForm";
    }

    @GetMapping("/signIn")
    public String signIn() {
        return "registrationForm";
    }

    @PostMapping("/signIn")
    public String addClient(@RequestParam String fullName,
                            @RequestParam java.time.LocalDate birthDate,
                            @RequestParam String passportNumber,
                            @RequestParam String address,
                            @RequestParam String phone,
                            @RequestParam String clientType,
                            @RequestParam String login,
                            @RequestParam String password) {
        try{
            Client client = new Client(fullName, birthDate, passportNumber, phone, address, clientType);
            clientRepository.addClient(client);
            client = clientRepository.getLastAddedClient();
            BankAccount bankAccount = new BankAccount(client.getId(), login, password);
            bankAccountRepository.addAccount(bankAccount);
            return "redirect:/success";
        }
        catch(Exception e){
            return "redirect:/failed";
        }
    }

    @GetMapping("/success")
    public String successfully(Model model) {
        model.addAttribute("message", "Operation successful");
        return "success";
    }

    @GetMapping("/failed")
    public String failed(Model model) {
        model.addAttribute("messageError", "Operation failed");
        return "failed";
    }
}
