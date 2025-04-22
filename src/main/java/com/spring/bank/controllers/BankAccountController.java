package com.spring.bank.controllers;

import com.spring.bank.models.BankAccount;
import com.spring.bank.models.BankCard;
import com.spring.bank.repositories.BankAccountRepository;
import com.spring.bank.repositories.BankCardRepository;
import com.spring.bank.repositories.ClientRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.security.Principal;

@Controller
public class BankAccountController {

    private ClientRepository clientRepository;
    private BankAccountRepository bankAccountRepository;
    private BankCardRepository bankCardRepository;

    private BankAccount bankAccount;

    public BankAccountController() {
        clientRepository = new ClientRepository();
        bankAccountRepository = new BankAccountRepository();
        bankCardRepository = new BankCardRepository();
    }

    @GetMapping("/dashboard")
    public String showMainPage(Principal principal, Model model) {
        BankAccount account;
        if(bankAccount == null) {
            String username = principal.getName();
            account = bankAccountRepository.findByLogin(username);
            bankAccount = account;
        } else{
            account = bankAccount;
        }

        model.addAttribute("account", account);
        model.addAttribute("client", clientRepository.getClientById(account.getClientId()));
        model.addAttribute("cards", bankCardRepository.findByBankAccountId(account.getId()));
        return "dashboard";
    }

    @GetMapping("/addCard")
    public String addCard(Model model) {
        BankCard card = new BankCard(bankAccount.getId());
        model.addAttribute("bankCard", card);
        return "newCard";
    }

    @PostMapping("/addCard")
    public String addCard(@ModelAttribute("bankCard") BankCard card,
                          @RequestParam("cardType") String cardType,
                          Principal principal,
                          Model model) {
        try{
            card.setCardType(cardType);
            card.setBankAccountId(bankAccount.getId());
            card.setBalance(0.0);
            bankCardRepository.addCard(card);

            BankAccount account = bankAccountRepository.findByLogin(principal.getName());
            model.addAttribute("message", "Operation successful");
            model.addAttribute("account", account);
            return "success";
        } catch (Exception e) {
            model.addAttribute("messageError", "Operation failed");
            return "failed";
        }
    }

    @GetMapping("/replenish")
    public String showReplenishForm(Model model) {
        model.addAttribute("cards", bankCardRepository.findByBankAccountId(bankAccount.getId()));
        return "replenish";
    }

    @GetMapping("/transfer")
    public String showTransferForm(Model model) {
        model.addAttribute("cards", bankCardRepository.findByBankAccountId(bankAccount.getId()));
        return "transfer";
    }

    @GetMapping("/mobile")
    public String showMobileForm(Model model) {
        model.addAttribute("cards", bankCardRepository.findByBankAccountId(bankAccount.getId()));
        return "mobile";
    }

    @GetMapping("/payments")
    public String showPaymentsForm() {
        return "payments";
    }
}
