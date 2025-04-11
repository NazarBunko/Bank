package com.spring.bank.controllers;

import com.spring.bank.models.Client;
import com.spring.bank.repositories.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/all")
    public List<Client> getAllClients() {
        return clientRepository.getAllClients();
    }

    @GetMapping("/{id}")
    public Client getClient(@PathVariable int id) {
        return clientRepository.getClientById(id);
    }

}
