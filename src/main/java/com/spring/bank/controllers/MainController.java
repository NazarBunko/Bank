package com.spring.bank.controllers;

import com.spring.bank.models.BankAccount;
import com.spring.bank.models.Client;
import com.spring.bank.repositories.BankAccountRepository;
import com.spring.bank.repositories.ClientRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


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
        return "mainPages/welcome";
    }

    @GetMapping("/logIn")
    public String logIn() {
        return "mainPages/loginForm";
    }

    @GetMapping("/signIn")
    public String signIn() {
        return "mainPages/registrationForm";
    }

    @GetMapping("/forgotPassword")
    public String forgotPassword() {
        return "mainPages/forgotPassword";
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
        return "messages/success";
    }

    @GetMapping("/failed")
    public String failed(Model model) {
        model.addAttribute("messageError", "Operation failed");
        return "messages/failed";
    }

    @GetMapping("/payments")
    public String showPaymentsForm() {
        return "sidebar/payments";
    }

    @GetMapping("/credit-terms")
    public String showCreditTermsForm() {
        return "sidebar/credit/creditTerms";
    }

    @GetMapping("/edit-name")
    public String showEditNameForm() {
        return "settings/editName";
    }

    @GetMapping("/change-password")
    public String showEditPasswordForm() {
        return "settings/editPassword";
    }

    @GetMapping("/change-login")
    public String showEditLoginForm() {
        return "settings/editLogin";
    }

    @GetMapping("/change-address")
    public String showEditAddressForm() {
        return "settings/editAddress";
    }

    @GetMapping("/change-phone")
    public String showEditPhoneForm() {
        return "settings/editPhone";
    }

    @GetMapping("/change-birthdate")
    public String showEditBirthdateForm() {
        return "settings/editBirthDate";
    }

    @GetMapping("/change-client-type")
    public String showEditClientTypeForm() {
        return "settings/editClientType";
    }

    @GetMapping("/change-passport")
    public String showEditPassportForm() {
        return "settings/editPassport";
    }
}
