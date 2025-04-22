package com.spring.bank.controllers;

import com.spring.bank.models.BankAccount;
import com.spring.bank.models.BankCard;
import com.spring.bank.models.Credit;
import com.spring.bank.models.PaymentTransaction;
import com.spring.bank.repositories.BankAccountRepository;
import com.spring.bank.repositories.BankCardRepository;
import com.spring.bank.repositories.ClientRepository;
import com.spring.bank.repositories.CreditRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;
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
    private CreditRepository creditRepository;

    private BankAccount bankAccount;

    public BankAccountController() {
        clientRepository = new ClientRepository();
        bankAccountRepository = new BankAccountRepository();
        bankCardRepository = new BankCardRepository();
        creditRepository = new CreditRepository();
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
        double monthlyPayment = bankCardRepository.credit(cardNumber, amount, InterestRate, TermMonths, bankAccount);

        if(monthlyPayment > 0){
            model.addAttribute("message", "Кредит оформлено! Щомісячний платіж: " + monthlyPayment + " грн");
            return "success";
        } else {
            model.addAttribute("messageError", "Помилка! Кредит не оформлено");
            return "failed";
        }
    }

    @GetMapping("/repayCredit")
    public String showRepayCreditForm(Model model) {
        model.addAttribute("cards", bankCardRepository.findByBankAccountId(bankAccount.getId()));
        model.addAttribute("credits", creditRepository.findAllByClientId(bankAccount.getClientId()));
        return "repayCredit";
    }

    @PostMapping("/repayCredit")
    public String repayCredit(@RequestParam String cardNumber,
                              @RequestParam int creditId,
                              @RequestParam double paymentAmount,
                              Model model) {

        Integer clientId = bankAccount.getClientId();
        if (clientId == null) {
            model.addAttribute("messageError", "Картку не знайдено");
            return "failed";
        }

        if(bankCardRepository.findCardByNumber(cardNumber).getBalance() < paymentAmount){
            model.addAttribute("messageError", "Виникла помилка при погашені кредиту!");
            return "failed";
        }
        boolean statusCredit = creditRepository.repayCredit(creditId, paymentAmount);
        boolean statusCard = bankCardRepository.repayCredit(paymentAmount, cardNumber);

        if(statusCredit && statusCard){
            model.addAttribute("message", "Погашення кредиту успішне!");
            return "success";
        } else {
            model.addAttribute("messageError", "Виникла помилка при погашені кредиту!");
            return "failed";
        }
    }
}
