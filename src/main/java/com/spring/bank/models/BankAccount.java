package com.spring.bank.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Entity
@Table(name = "Bank_Account")
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @JoinColumn(name = "ClientID", nullable = false)
    private Integer clientId;

    @NotNull
    @Length(min = 4, max = 100)
    @Column(name = "Login", nullable = false)
    private String login;

    @NotBlank
    @Length(min = 4, max = 100)
    @Column(name = "Password", nullable = false)
    private String password;

    @DecimalMin(value = "0.00")
    @Column(name = "Balance")
    private Double balance;

    public BankAccount() {}

    public BankAccount(Integer clientId, String login, String password) {
        this.login = login;
        this.password = password;
        this.balance = 0.0;
        this.clientId = clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClient(Integer clientId) {
        this.clientId = clientId;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "id=" + id +
                ", client=" + clientId +
                ", balance=" + balance +
                ", password='********'" +
                '}';
    }
}
