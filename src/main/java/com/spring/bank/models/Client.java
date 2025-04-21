package com.spring.bank.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Entity
@Table(name = "Client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Length(min = 2, max = 255)
    @Column(name = "FullName", nullable = false)
    private String fullName;

    @NotNull
    @Column(name = "BirthDate", nullable = false)
    private java.time.LocalDate birthDate;

    @NotNull
    @Length(min = 6, max = 20)
    @Column(name = "PassportNumber", nullable = false, unique = true)
    private String passportNumber;

    @Column(name = "Address")
    private String address;

    @Length(max = 20)
    @Column(name = "Phone")
    private String phone;

    @Length(max = 50)
    @Column(name = "ClientType")
    private String clientType;

    public Client() {}

    public Client(String fullName, java.time.LocalDate birthDate, String passportNumber, String phone, String address, String clientType) {
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.passportNumber = passportNumber;
        this.address = address;
        this.clientType = clientType;
        this.phone = phone;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public java.time.LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(java.time.LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", birthDate=" + birthDate +
                ", passportNumber='" + passportNumber + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", clientType='" + clientType + '\'' +
                '}';
    }
}
