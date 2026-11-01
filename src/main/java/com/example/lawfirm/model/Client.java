package com.example.lawfirm.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String clientCode;

    private String email;
    private String company;

    public Client() {
    }

    public Client(UUID id, String firstName, String lastName, String clientCode, String email, String company) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.clientCode = clientCode;
        this.email = email;
        this.company = company;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
