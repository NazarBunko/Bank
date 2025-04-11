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

    @Length(max = 50)
    @Column(name = "AccountType")
    private String accountType;

    public BankAccount() {}

    public BankAccount(Integer clientId, String login, String password, String accountType) {
        this.accountType = accountType;
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

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
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
                ", accountType='" + accountType + '\'' +
                ", password='********'" +
                '}';
    }
}
