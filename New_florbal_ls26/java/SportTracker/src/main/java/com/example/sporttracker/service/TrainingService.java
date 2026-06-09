package com.example.sporttracker.service;

import com.example.sporttracker.entity.Attendance;
import com.example.sporttracker.entity.Player;
import com.example.sporttracker.entity.Training;
import com.example.sporttracker.entity.TrainingStatus;
import com.example.sporttracker.exception.*;
import com.example.sporttracker.repository.AttendanceRepository;
import com.example.sporttracker.repository.PlayerRepository;
import com.example.sporttracker.repository.TrainingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingService {

    private final TrainingRepository trainingRepository;
    private final PlayerRepository playerRepository;
    private final AttendanceRepository attendanceRepository;

    public TrainingService(
            TrainingRepository trainingRepository,
            PlayerRepository playerRepository,
            AttendanceRepository attendanceRepository
    ) {
        this.trainingRepository = trainingRepository;
        this.playerRepository = playerRepository;
        this.attendanceRepository = attendanceRepository;
    }

    public List<Training> getTrainings() {
        return trainingRepository.findAll();
    }

    public Training getTrainingById(Long id) {
        return trainingRepository.findById(id)
                .orElseThrow(() -> new TrainingNotFoundException("Training not found"));
    }

    public Training createTraining(Training training) {
        return trainingRepository.save(training);
    }

    public void registerPlayerToTraining(Long trainingId, Long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found"));

        Training training = trainingRepository.findById(trainingId)
                .orElseThrow(() -> new TrainingNotFoundException("Training not found"));

        if (training.getStatus() == TrainingStatus.FINISHED) {
            throw new TrainingClosedException("Training is already finished");
        }

        if (attendanceRepository.existsByPlayerAndTraining(player, training)) {
            throw new AttendanceAlreadyExistsException("Attendance already exists");
        }

        Attendance attendance = new Attendance();
        attendance.setPlannedAttendance(true);
        attendance.setActualAttendance(false);
        attendance.setPlayer(player);
        attendance.setTraining(training);

        attendanceRepository.save(attendance);
    }

    public void unregisterPlayerFromTraining(Long trainingId, Long playerId) {
        Training training = trainingRepository.findById(trainingId)
                .orElseThrow(() -> new TrainingNotFoundException("Training not found"));

        if (training.getStatus() == TrainingStatus.FINISHED) {
            throw new TrainingClosedException("Training is already finished");
        }

        Attendance attendance = attendanceRepository.findByPlayer_IdAndTraining_Id(playerId, trainingId)
                .orElseThrow(() -> new AttendanceNotFoundException("Attendance not found for training " + trainingId + " and player " + playerId));

        attendanceRepository.delete(attendance);
    }

    public long getRegisteredPlayersCount(Long trainingId) {
        if (!trainingRepository.existsById(trainingId)) {
            throw new TrainingNotFoundException("Training not found");
        }
        return attendanceRepository.countByTraining_Id(trainingId);
    }

    public Training closeTraining(Long trainingId) {
        Training training = trainingRepository.findById(trainingId)
                .orElseThrow(() -> new TrainingNotFoundException("Training not found"));
        training.setStatus(TrainingStatus.FINISHED);
        return trainingRepository.save(training);
    }
}
