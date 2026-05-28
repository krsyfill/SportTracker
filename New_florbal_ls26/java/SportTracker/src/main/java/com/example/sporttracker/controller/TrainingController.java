package com.example.sporttracker.controller;

import com.example.sporttracker.entity.Training;
import com.example.sporttracker.repository.TrainingRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trainings")
public class TrainingController {

    private final TrainingRepository trainingRepository;

    public TrainingController(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    @GetMapping
    public List<Training> getTrainings() {
        return trainingRepository.findAll();
    }

    @GetMapping("/{id}")
    public Training getTrainingById(@PathVariable Long id) {

        return trainingRepository.findById(id)
                .orElse(null);
    }

    @PostMapping
    public Training createTraining(
            @RequestBody Training training
    ) {

        return trainingRepository.save(training);
    }
}