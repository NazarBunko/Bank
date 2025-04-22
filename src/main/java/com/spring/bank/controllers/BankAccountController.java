package com.spring.bank.controllers;

import com.spring.bank.models.BankAccount;
import com.spring.bank.models.BankCard;
import com.spring.bank.models.Credit;
import com.spring.bank.repositories.BankAccountRepository;
import com.spring.bank.repositories.BankCardRepository;
import com.spring.bank.repositories.ClientRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

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

    @GetMapping("/credit")
    public String showCreditForm(Model model) {
        List<BankCard> allCards = bankCardRepository.findByBankAccountId(bankAccount.getId());
        List<BankCard> creditCards = new ArrayList<>();
        for(int i = 0; i < allCards.size(); i++){
            if(allCards.get(i).getCardType().equals("Кредитна")){
                creditCards.add(allCards.get(i));
            }
        }
        model.addAttribute("cards", creditCards);
        return "credit";
    }

    @PostMapping("/credit")
    public String newCredit(@RequestParam String cardNumber,
                            @RequestParam double amount,
                            @RequestParam double InterestRate,
                            @RequestParam int TermMonths,
                            Model model) {

        Integer clientId = bankAccount.getClientId();

        if (clientId == null) {
            model.addAttribute("messageError", "Картку не знайдено");
            return "failed";
        }

        double monthlyRate   = InterestRate / 100.0 / 12.0;
        double monthlyPayment = (amount * monthlyRate) /
                (1.0 - Math.pow(1.0 + monthlyRate, -TermMonths));

        monthlyPayment = Math.round(monthlyPayment * 100.0) / 100.0;

        Credit credit = new Credit();
        credit.setClient(clientId);
        credit.setCardNumber(cardNumber);
        credit.setAmount(amount);
        credit.setInterestRate(InterestRate);
        credit.setTermMonths(TermMonths);
        credit.setMonthlyPayment(monthlyPayment);
        credit.setStatus("Відкритий");

        boolean status = bankCardRepository.credit(cardNumber, amount, credit);

        System.out.println(credit);
        if(status){
            model.addAttribute("message", "Кредит оформлено! Щомісячний платіж: " + monthlyPayment + " грн");
            return "success";
        } else {
            model.addAttribute("messageError", "Помилка! Кредит не оформлено");
            return "failed";
        }

    }

}
