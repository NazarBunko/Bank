package com.spring.bank.models;

import lombok.Data;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.Random;

@Data
@Entity
@Table(name = "Bank_Card")
public class BankCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @JoinColumn(name = "BankAccountID")
    private Integer bankAccountId;

    @NotNull
    @Column(name = "CardNumber", length = 16, unique = true)
    private String cardNumber;

    @NotNull
    @Column(name = "CVV")
    private Integer cvv;

    @NotNull
    @Column(name = "EndDate")
    private java.time.LocalDate endDate;

    @NotNull
    @Column(name = "CardType")
    private String cardType;

    @DecimalMin(value = "0.00")
    @Column(name = "Balance")
    private Double balance;

    public BankCard() {}

    public BankCard(Integer bankAccountId) {
        this.bankAccountId = bankAccountId;

        StringBuilder cardNumber = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 16; i++) {
            cardNumber.append(random.nextInt(10)); // від 0 до 9
        }
        this.cardNumber = cardNumber.toString();
        this.cardType = null;

        this.endDate = LocalDate.now().plusYears(5);
        this.cvv = 100 + random.nextInt(900);
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

    public Integer getCvv() {
        return cvv;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    @Override
    public String toString() {
        return "BankCard{" +
                "id=" + id +
                ", bankAccountId=" + bankAccountId +
                ", cardNumber='" + cardNumber + '\'' +
                ", cvv=" + cvv +
                ", endDate=" + endDate +
                ", cardType='" + cardType + '\'' +
                ", balance=" + balance +
                '}';
    }
}
