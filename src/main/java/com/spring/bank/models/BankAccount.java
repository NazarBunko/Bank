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
    @Column(name = "Login", nullable = false, unique = true)
    private String login;

    @NotBlank
    @Length(min = 4, max = 100)
    @Column(name = "Password", nullable = false)
    private String password;

    @NotNull
    @Column(name = "Role", nullable = false)
    private String role; // ROLE_USER або ROLE_ADMIN

    public BankAccount() {}

    public BankAccount(Integer clientId, String login, String password, String role) {
        this.clientId = clientId;
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public Integer getClientId() {
        return clientId;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", login='" + login + '\'' +
                ", role='" + role + '\'' +
                ", password='********'" +
                '}';
    }
}
