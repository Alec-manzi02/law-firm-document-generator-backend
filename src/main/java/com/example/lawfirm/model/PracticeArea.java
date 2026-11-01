package com.example.lawfirm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "practice_area")
public class PracticeArea {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    @ManyToMany(mappedBy = "practiceAreas")
    @JsonIgnore
    private List<Attorney> attorneys = new ArrayList<>();

    public PracticeArea() {
    }

    public PracticeArea(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Attorney> getAttorneys() {
        return attorneys;
    }

    public void setAttorneys(List<Attorney> attorneys) {
        this.attorneys = attorneys;
    }
}
