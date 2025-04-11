package com.spring.bank.controllers;

import com.spring.bank.models.BankAccount;
import com.spring.bank.models.Client;
import com.spring.bank.repositories.BankAccountRepository;
import com.spring.bank.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class MainController {

    private final ClientRepository clientRepository;
    private final BankAccountRepository bankAccountRepository;

    @Autowired
    public MainController(ClientRepository clientRepository, BankAccountRepository bankAccountRepository) {
        this.clientRepository = clientRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    @GetMapping("/")
    public String welcome() {
        return "welcome";
    }

    @GetMapping("/signIn")
    public String newAccount() {
        return "registrationForm";
    }

    @GetMapping("/logIn")
    public String login() {
        return "loginForm";
    }

    @PostMapping("/signIn")
    public String newClient(@RequestParam String fullName,
                            @RequestParam java.time.LocalDate birthDate,
                            @RequestParam String passportNumber,
                            @RequestParam String address,
                            @RequestParam String phone,
                            @RequestParam String clientType,
                            @RequestParam String login,
                            @RequestParam String password) {

        try {
            Client client = new Client(fullName, birthDate, passportNumber, phone, address, clientType);
            clientRepository.addClient(client);
            client = clientRepository.getLastAddedClient();
            BankAccount bankAccount = new BankAccount(client.getId(), login, password);
            bankAccountRepository.addAccount(bankAccount);
            return "redirect:/app/success";
        } catch (Exception e) {
            return "redirect:/app/failed";
        }
    }

    @GetMapping("/success")
    public String successPage(Model model) {
        model.addAttribute("message", "Operation successful");
        return "successfully";
    }

    @GetMapping("/failed")
    public String failedPage(Model model) {
        model.addAttribute("messageError", "Something went wrong");
        return "failed";
    }
}
