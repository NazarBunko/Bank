package com.spring.bank.models;

import lombok.Data;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Data
@Entity
@Table(name = "Bank_Card")
public class BankCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "BankAccountID", nullable = false)
    private Integer bankAccountId;

    @Column(name = "CardNumber", length = 16, unique = true, nullable = false)
    private String cardNumber;

    @Column(name = "CardType", nullable = false)
    private String cardType;

    @DecimalMin(value = "0.00")
    @Column(name = "Balance")
    private Double balance;

    public BankCard() {}

    public BankCard(Integer bankAccountId, String cardNumber, String cardType) {
        this.bankAccountId = bankAccountId;
        this.cardNumber = cardNumber;
        this.cardType = cardType;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public Integer getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(Integer bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    @Override
    public String toString() {
        return "BankCard{" +
                "bankAccountId=" + bankAccountId +
                ", cardNumber='" + cardNumber + '\'' +
                ", cardType='" + cardType + '\'' +
                ", balance=" + balance +
                ", id=" + id +
                '}';
    }
}
