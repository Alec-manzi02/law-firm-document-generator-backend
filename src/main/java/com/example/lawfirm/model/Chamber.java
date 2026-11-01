package com.example.lawfirm.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "chamber")
public class Chamber {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private int chamberNumber;

    private String name;

    public Chamber() {
    }

    public Chamber(UUID id, int chamberNumber, String name) {
        this.id = id;
        this.chamberNumber = chamberNumber;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getChamberNumber() {
        return chamberNumber;
    }

    public void setChamberNumber(int chamberNumber) {
        this.chamberNumber = chamberNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
