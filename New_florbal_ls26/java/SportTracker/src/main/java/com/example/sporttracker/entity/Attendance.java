package com.example.sporttracker.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "attendance")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean plannedAttendance;

    private Boolean actualAttendance;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne
    @JoinColumn(name = "training_id")
    private Training training;

    public Attendance() {
    }

    public Long getId() {
        return id;
    }

    public Boolean getPlannedAttendance() {
        return plannedAttendance;
    }

    public Boolean getActualAttendance() {
        return actualAttendance;
    }

    public Player getPlayer() {
        return player;
    }

    public Training getTraining() {
        return training;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPlannedAttendance(Boolean plannedAttendance) {
        this.plannedAttendance = plannedAttendance;
    }

    public void setActualAttendance(Boolean actualAttendance) {
        this.actualAttendance = actualAttendance;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setTraining(Training training) {
        this.training = training;
    }
}