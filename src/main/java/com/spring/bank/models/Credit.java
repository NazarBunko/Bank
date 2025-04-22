package com.spring.bank.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Entity
@Table(name = "Credit")
public class Credit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @JoinColumn(name = "ClientID", nullable = false)
    private Integer clientId;

    @NotNull
    @JoinColumn(name = "BankAccountID", nullable = false)
    private Integer bankAccountId;

    @NotNull
    @Column(name = "CardNumber", nullable = false)
    private String cardNumber;

    @NotNull
    @Positive
    @Column(name = "PrincipalAmount")
    private Double principalAmount;

    @NotNull
    @PositiveOrZero
    @Column(name = "Amount")
    private Double amount;

    @NotNull
    @DecimalMin(value = "0.01")
    @DecimalMax(value = "100.00")
    @Column(name = "InterestRate", nullable = false)
    private Double interestRate;

    @NotNull
    @Positive
    @Column(name = "TermMonths", nullable = false)
    private Integer termMonths;

    @Length(max = 50)
    @Column(name = "Status")
    private String status;

    @Positive
    @Column(name = "MonthlyPayment")
    private Double monthlyPayment;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Integer getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(Integer bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Double getPrincipalAmount() {
        return principalAmount;
    }

    public void setPrincipalAmount(Double principalAmount) {
        this.principalAmount = principalAmount;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getMonthlyPayment() {
        return monthlyPayment;
    }

    public void setMonthlyPayment(Double monthlyPayment) {
        this.monthlyPayment = monthlyPayment;
    }

    public Integer getTermMonths() {
        return termMonths;
    }

    public void setTermMonths(Integer termMonths) {
        this.termMonths = termMonths;
    }

    @Override
    public String toString() {
        return "Credit{" +
                "id=" + id +
                ", client=" + clientId +
                ", account=" + bankAccountId +
                ", amount=" + amount +
                ", interestRate=" + interestRate +
                ", termMonths=" + termMonths +
                ", status='" + status + '\'' +
                ", monthlyPayment=" + monthlyPayment +
                '}';
    }
}

