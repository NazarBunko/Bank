package com.spring.bank.controllers;

import com.spring.bank.models.BankAccount;
import com.spring.bank.repositories.BankAccountRepository;
import com.spring.bank.repositories.BankCardRepository;
import com.spring.bank.repositories.ClientRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BankCardController {

    private BankCardRepository bankCardRepository;


    public BankCardController() {
        bankCardRepository = new BankCardRepository();
    }

    @PostMapping("/replenish")
    public String replenish(
            @RequestParam("card") String card,
            @RequestParam("amount") Double amount,
            @RequestParam("paymentMethod") String method,
            Model model) {
        boolean success = bankCardRepository.replenishCard(card, amount, method);
        if (success) {
            model.addAttribute("message", "Поповнення успішне!");
            return "success";
        } else {
            model.addAttribute("messageError", "Помилка під час поповнення. Спробуйте ще раз.");
            return "failed";
        }
    }

    @PostMapping("/transfer")
    public String transfer(
            @RequestParam("fromCard") String fromCard,
            @RequestParam("toCard") String toCard,
            @RequestParam("amount") Double amount,
            @RequestParam("description") String description,
            Model model) {

        boolean success = bankCardRepository.transfer(fromCard, toCard, amount, description);
        if (success) {
            model.addAttribute("message", "Переказ успішно виконано!");
            return "success";
        } else {
            model.addAttribute("messageError", "Помилка під час переказу. Перевірте дані та спробуйте ще раз.");
            return "failed";
        }
    }
}
