package com.example.sporttracker.controller;

import com.example.sporttracker.entity.Training;
import com.example.sporttracker.service.TrainingService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/trainings")
public class TrainingController {

    private final TrainingService trainingService;

    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @GetMapping
    public List<Training> getTrainings() {
        return trainingService.getTrainings();
    }

    @GetMapping("/{id}")
    public Training getTrainingById(@PathVariable Long id) {

        return trainingService.getTrainingById(id);
    }

    @PostMapping
    public Training createTraining(
            @Valid @RequestBody Training training
    ) {

        return trainingService.createTraining(training);
    }

    @PostMapping("/{trainingId}/register/{playerId}")
    public void registerPlayerToTraining(
            @PathVariable Long trainingId,
            @PathVariable Long playerId
    ) {
        trainingService.registerPlayerToTraining(trainingId, playerId);
    }

    @DeleteMapping("/{trainingId}/register/{playerId}")
    public void unregisterPlayerFromTraining(
            @PathVariable Long trainingId,
            @PathVariable Long playerId
    ) {
        trainingService.unregisterPlayerFromTraining(trainingId, playerId);
    }

    @GetMapping("/{id}/registered-count")
    public Map<String, Object> getRegisteredPlayersCount(@PathVariable Long id) {
        long count = trainingService.getRegisteredPlayersCount(id);
        Map<String, Object> response = new HashMap<>();
        response.put("trainingId", id);
        response.put("registeredPlayers", count);
        return response;
    }

    @PutMapping("/{id}/close")
    public Training closeTraining(@PathVariable Long id) {
        return trainingService.closeTraining(id);
    }
}