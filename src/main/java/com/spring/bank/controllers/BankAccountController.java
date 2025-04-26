package com.spring.bank.controllers;

import com.spring.bank.models.*;
import com.spring.bank.repositories.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class BankAccountController {

    private ClientRepository clientRepository;
    private BankAccountRepository bankAccountRepository;
    private BankCardRepository bankCardRepository;
    private CreditRepository creditRepository;
    private TransactionRepository transactionRepository;

    private final String successMessage = "messages/success";
    private final String failedMessage = "messages/failed";

    private BankAccount bankAccount;

    public BankAccountController() {
        clientRepository = new ClientRepository();
        bankAccountRepository = new BankAccountRepository();
        bankCardRepository = new BankCardRepository();
        creditRepository = new CreditRepository();
        transactionRepository = new TransactionRepository();
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

        List<BankCard> cards = bankCardRepository.findByBankAccountId(account.getId());
        model.addAttribute("account", account);
        model.addAttribute("client", clientRepository.getClientById(account.getClientId()));
        model.addAttribute("cards", cards);
        model.addAttribute("transactions", transactionRepository.findTransactionsByCards(cards));
        return "mainPages/dashboard";
    }

    @PostMapping("/forgotPassword")
    public String forgotPassword(@RequestParam String username, @RequestParam String password) {
        try{
            bankAccountRepository.updatePassword(username, password);
            bankAccount = bankAccountRepository.findByLogin(username);
            return "redirect:/success";
        }
        catch(Exception e){
            return "redirect:/failed";
        }
    }

    @GetMapping("/addCard")
    public String addCard(Model model) {
        BankCard card = new BankCard(bankAccount.getId());
        model.addAttribute("bankCard", card);
        return "sidebar/newCard";
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
            model.addAttribute("message", "Операція успішна!");
            model.addAttribute("account", account);
            return successMessage;
        } catch (Exception e) {
            model.addAttribute("messageError", "Виникла помилка!");
            return failedMessage;
        }
    }

    @GetMapping("/replenish")
    public String showReplenishForm(Model model) {
        model.addAttribute("cards", bankCardRepository.findByBankAccountId(bankAccount.getId()));
        return "sidebar/replenish";
    }

    @GetMapping("/transfer")
    public String showTransferForm(Model model) {
        model.addAttribute("cards", bankCardRepository.findByBankAccountId(bankAccount.getId()));
        return "sidebar/transfer";
    }

    @GetMapping("/mobile")
    public String showMobileForm(Model model) {
        model.addAttribute("cards", bankCardRepository.findByBankAccountId(bankAccount.getId()));
        return "sidebar/mobile";
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
        return "sidebar/credit/credit";
    }

    @GetMapping("/logOut")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        bankAccount = null;

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        SecurityContextHolder.clearContext();

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        return "redirect:/";
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
            return successMessage;
        } else {
            model.addAttribute("messageError", "Помилка! Кредит не оформлено");
            return failedMessage;
        }
    }

    @GetMapping("/repayCredit")
    public String showRepayCreditForm(Model model) {
        model.addAttribute("cards", bankCardRepository.findByBankAccountId(bankAccount.getId()));
        model.addAttribute("credits", creditRepository.findAllByClientId(bankAccount.getClientId()));
        return "sidebar/credit/repayCredit";
    }

    @PostMapping("/repayCredit")
    public String repayCredit(@RequestParam String cardNumber,
                              @RequestParam int creditId,
                              @RequestParam double paymentAmount,
                              Model model) {

        Integer clientId = bankAccount.getClientId();
        if (clientId == null) {
            model.addAttribute("messageError", "Картку не знайдено");
            return failedMessage;
        }

        if(bankCardRepository.findCardByNumber(cardNumber).getBalance() < paymentAmount){
            model.addAttribute("messageError", "Виникла помилка при погашені кредиту!");
            return failedMessage;
        }
        boolean statusCredit = creditRepository.repayCredit(creditId, paymentAmount);
        boolean statusCard = bankCardRepository.repayCredit(paymentAmount, cardNumber);

        if(statusCredit && statusCard){
            model.addAttribute("message", "Погашення кредиту успішне!");
            return successMessage;
        } else {
            model.addAttribute("messageError", "Виникла помилка при погашені кредиту!");
            return failedMessage;
        }
    }

    @GetMapping("/analytics")
    public String showAnalyticsForm(Model model) {
        List<BankCard> cards = bankCardRepository.findByBankAccountId(bankAccount.getId());
        List<Double> expenses = transactionRepository.getDailyTransactionSumsByCards(cards, true);
        List<Double> deposits = transactionRepository.getDailyTransactionSumsByCards(cards, false);

        System.out.println(expenses);
        System.out.println(deposits);

        model.addAttribute("expenses", expenses);
        model.addAttribute("deposits", deposits);

        return "sidebar/analytics";
    }

    @PostMapping("/updateAddress")
    public String updateAddress(@RequestParam String address,
                             @RequestParam String password,
                             Model model) {

        Client client = clientRepository.getClientById(bankAccount.getClientId());

        if (clientRepository.updateClientAddress(client, password, address, bankAccount)) {
            model.addAttribute("message", "Адресу успішно змінено!");
            return successMessage;
        }

        model.addAttribute("messageError", "Виникла помилка при зміні адреси!");
        return failedMessage;
    }

    @PostMapping("/updateBirthDate")
    public String updateBirthDate(@RequestParam LocalDate birthDate,
                                @RequestParam String password,
                                Model model) {

        Client client = clientRepository.getClientById(bankAccount.getClientId());

        if (clientRepository.updateClientBirthDate(client, password, birthDate, bankAccount)) {
            model.addAttribute("message", "Дату народження успішно змінено!");
            return successMessage;
        }

        model.addAttribute("messageError", "Виникла помилка при зміні дати народження!");
        return failedMessage;
    }

    @PostMapping("/updateClientType")
    public String updateClientType(@RequestParam String type,
                                @RequestParam String password,
                                Model model) {
        System.out.println(bankAccount);
        Client client = clientRepository.getClientById(bankAccount.getClientId());

        if (clientRepository.updateClientType(client, password, type, bankAccount)) {
            model.addAttribute("message", "Тип клієнта успішно змінено!");
            return successMessage;
        }

        model.addAttribute("messageError", "Виникла помилка при зміні типу клієнта!");
        return failedMessage;
    }

    @PostMapping("/updateLogin")
    public String updateAccountLogin(@RequestParam String login,
                                    @RequestParam String password,
                                    Model model) {

        if (bankAccountRepository.updateAccountLogin(password, login, bankAccount)) {
            model.addAttribute("message", "Логін успішно змінено!");
            return successMessage;
        }

        model.addAttribute("messageError", "Виникла помилка при зміні логіну!");
        return failedMessage;
    }

    @PostMapping("/updatePassport")
    public String updateClientPassport(@RequestParam String passport,
                                     @RequestParam String password,
                                     Model model) {

        Client client = clientRepository.getClientById(bankAccount.getClientId());

        if (clientRepository.updateClientPassport(client, password, passport, bankAccount)) {
            model.addAttribute("message", "Номер паспорта успішно змінено!");
            return successMessage;
        }

        model.addAttribute("messageError", "Виникла помилка при зміні номера паспорта!");
        return failedMessage;
    }

    @PostMapping("/updatePhone")
    public String updateClientPhone(@RequestParam String phone,
                                       @RequestParam String password,
                                       Model model) {

        Client client = clientRepository.getClientById(bankAccount.getClientId());

        if (clientRepository.updateClientPhone(client, password, phone, bankAccount)) {
            model.addAttribute("message", "Номер телефона успішно змінено!");
            return successMessage;
        }

        model.addAttribute("messageError", "Виникла помилка при зміні номера телефона!");
        return failedMessage;
    }

    @PostMapping("/updateName")
    public String updateName(@RequestParam String newName,
                             @RequestParam String password,
                             Model model) {

        Client client = clientRepository.getClientById(bankAccount.getClientId());

        if (clientRepository.updateClientName(client, password, newName, bankAccount)) {
            model.addAttribute("message", "Ім'я успішно змінено!");
            return successMessage;
        }

        model.addAttribute("messageError", "Виникла помилка при зміні імені!");
        return failedMessage;
    }

    @PostMapping("/updatePassword")
    public String updatePassword(@RequestParam String newPassword,
                             @RequestParam String password,
                             Model model) {

        if (bankAccountRepository.updateAccountPassword(password, newPassword, bankAccount)) {
            model.addAttribute("message", "Пароль успішно змінено!");
            return successMessage;
        }

        model.addAttribute("messageError", "Виникла помилка при зміні пароля!");
        return failedMessage;
    }
}
