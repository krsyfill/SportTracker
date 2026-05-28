package com.example.sporttracker.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "trainings")
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate trainingDate;

    public Training() {
    }

    public Long getId() {
        return id;
    }

    public LocalDate getTrainingDate() {
        return trainingDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTrainingDate(LocalDate trainingDate) {
        this.trainingDate = trainingDate;
    }
}