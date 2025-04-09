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
    @ManyToOne
    @JoinColumn(name = "ClientID", nullable = false)
    private Client client;

    @NotNull
    @Positive
    @Column(name = "Amount", nullable = false)
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
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
                ", client=" + client +
                ", amount=" + amount +
                ", interestRate=" + interestRate +
                ", termMonths=" + termMonths +
                ", status='" + status + '\'' +
                ", monthlyPayment=" + monthlyPayment +
                '}';
    }
}

