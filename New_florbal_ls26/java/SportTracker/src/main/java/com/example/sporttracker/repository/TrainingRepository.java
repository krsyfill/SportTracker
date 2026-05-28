package com.example.sporttracker.repository;

import com.example.sporttracker.entity.Training;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingRepository
        extends JpaRepository<Training, Long> {

}