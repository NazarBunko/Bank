package com.spring.bank.controllers;

import com.spring.bank.models.Client;
import com.spring.bank.repositories.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/app")
public class MainController {

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to the bank";
    }

    @GetMapping("/clients")
    public List<Client> getAllClients() {
        return clientRepository.getAllClients();
    }

    @GetMapping("/{id}")
    public Client getClient(@PathVariable int id) {
        return clientRepository.getClientById(id);
    }

}
