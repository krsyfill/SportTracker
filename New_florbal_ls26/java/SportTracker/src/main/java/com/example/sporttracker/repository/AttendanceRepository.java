package com.example.sporttracker.repository;

import com.example.sporttracker.entity.Attendance;
import com.example.sporttracker.entity.Player;
import com.example.sporttracker.entity.Training;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttendanceRepository
        extends JpaRepository<Attendance, Long> {

    boolean existsByPlayerAndTraining(Player player, Training training);

    long countByPlayer_Id(Long playerId);

    long countByPlayer_IdAndActualAttendanceTrue(Long playerId);
    
    Optional<Attendance> findByPlayer_IdAndTraining_Id(Long playerId, Long trainingId);

    long countByTraining_Id(Long trainingId);
}