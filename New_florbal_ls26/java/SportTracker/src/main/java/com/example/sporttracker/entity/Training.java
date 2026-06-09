package com.example.sporttracker.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(name = "trainings")
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDate trainingDate;

    @Enumerated(EnumType.STRING)
    private TrainingStatus status = TrainingStatus.PLANNED;

    public Training() {
    }

    public Long getId() {
        return id;
    }

    public LocalDate getTrainingDate() {
        return trainingDate;
    }

    public TrainingStatus getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTrainingDate(LocalDate trainingDate) {
        this.trainingDate = trainingDate;
    }

    public void setStatus(TrainingStatus status) {
        this.status = status;
    }
}