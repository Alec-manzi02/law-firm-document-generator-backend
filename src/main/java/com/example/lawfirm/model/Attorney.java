package com.example.lawfirm.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "attorney")
public class Attorney {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String firstName;
    private String lastName;
    private String barNumber;

    @ManyToOne
    @JoinColumn(name = "chamber_id", nullable = true)
    private Chamber chamber;

    @ManyToMany
    @JoinTable(
        name = "attorney_practice_area",
        joinColumns = @JoinColumn(name = "attorney_id"),
        inverseJoinColumns = @JoinColumn(name = "practice_area_id")
    )
    private List<PracticeArea> practiceAreas = new ArrayList<>();

    public Attorney() {
    }

    public Attorney(UUID id, String firstName, String lastName, String barNumber, Chamber chamber, List<PracticeArea> practiceAreas) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.barNumber = barNumber;
        this.chamber = chamber;
        this.practiceAreas = practiceAreas != null ? practiceAreas : new ArrayList<>();
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

    public String getBarNumber() {
        return barNumber;
    }

    public void setBarNumber(String barNumber) {
        this.barNumber = barNumber;
    }

    public Chamber getChamber() {
        return chamber;
    }

    public void setChamber(Chamber chamber) {
        this.chamber = chamber;
    }

    public List<PracticeArea> getPracticeAreas() {
        return practiceAreas;
    }

    public void setPracticeAreas(List<PracticeArea> practiceAreas) {
        this.practiceAreas = practiceAreas;
    }
}
