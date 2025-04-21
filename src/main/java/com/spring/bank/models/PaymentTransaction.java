package com.spring.bank.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Payment_Transaction")
public class PaymentTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "Sender", nullable = false)
    private String sender;

    @NotNull
    @Column(name = "Receiver", nullable = false)
    private String receiver;

    @NotNull
    @Column(name = "Amount", nullable = false)
    private double amount;

    @NotNull
    @Column(name = "Note", nullable = false)
    private String note;

    @NotNull
    @Column(name = "TransactionDate", nullable = false)
    private java.time.LocalDate TransactionDate;

    public PaymentTransaction() {}

    public PaymentTransaction(String sender, String receiver, double amount, String note) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.note = note;
        TransactionDate = java.time.LocalDate.now();
    }

    public LocalDate getTransactionDate() {
        return TransactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        TransactionDate = transactionDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    @Override
    public String toString() {
        return "Procedure{" +
                "id=" + id +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", amount='" + amount + '\'' +
                ", note='" + note + '\'' +
                ", TransactionDate=" + TransactionDate +
                '}';
    }
}