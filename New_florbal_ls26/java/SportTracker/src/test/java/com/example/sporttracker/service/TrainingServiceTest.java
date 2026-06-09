package com.example.sporttracker.service;

import com.example.sporttracker.entity.Attendance;
import com.example.sporttracker.entity.Player;
import com.example.sporttracker.entity.Training;
import com.example.sporttracker.entity.TrainingStatus;
import com.example.sporttracker.exception.*;
import com.example.sporttracker.repository.AttendanceRepository;
import com.example.sporttracker.repository.PlayerRepository;
import com.example.sporttracker.repository.TrainingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainingServiceTest {

    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private AttendanceRepository attendanceRepository;

    @InjectMocks
    private TrainingService trainingService;

    @Test
    void shouldRegisterPlayerSuccessfully() {
        // Given
        Long playerId = 1L;
        Long trainingId = 1L;
        Player player = new Player();
        Training training = new Training();

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
        when(trainingRepository.findById(trainingId)).thenReturn(Optional.of(training));
        when(attendanceRepository.existsByPlayerAndTraining(player, training)).thenReturn(false);

        // When
        trainingService.registerPlayerToTraining(trainingId, playerId);

        // Then
        verify(attendanceRepository, times(1)).save(any(Attendance.class));
    }

    @Test
    void shouldThrowPlayerNotFoundException() {
        // Given
        Long playerId = 1L;
        Long trainingId = 1L;

        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(PlayerNotFoundException.class, () -> {
            trainingService.registerPlayerToTraining(trainingId, playerId);
        });

        verify(attendanceRepository, never()).save(any());
    }

    @Test
    void shouldThrowTrainingNotFoundException() {
        // Given
        Long playerId = 1L;
        Long trainingId = 1L;
        Player player = new Player();

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
        when(trainingRepository.findById(trainingId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(TrainingNotFoundException.class, () -> {
            trainingService.registerPlayerToTraining(trainingId, playerId);
        });

        verify(attendanceRepository, never()).save(any());
    }

    @Test
    void shouldThrowAttendanceAlreadyExistsException() {
        // Given
        Long playerId = 1L;
        Long trainingId = 1L;
        Player player = new Player();
        Training training = new Training();

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
        when(trainingRepository.findById(trainingId)).thenReturn(Optional.of(training));
        when(attendanceRepository.existsByPlayerAndTraining(player, training)).thenReturn(true);

        // When & Then
        assertThrows(AttendanceAlreadyExistsException.class, () -> {
            trainingService.registerPlayerToTraining(trainingId, playerId);
        });

        verify(attendanceRepository, never()).save(any());
    }

    @Test
    void shouldUnregisterPlayerSuccessfully() {
        // Given
        Long playerId = 1L;
        Long trainingId = 1L;
        Training training = new Training();
        Attendance attendance = new Attendance();

        when(trainingRepository.findById(trainingId)).thenReturn(Optional.of(training));
        when(attendanceRepository.findByPlayer_IdAndTraining_Id(playerId, trainingId)).thenReturn(Optional.of(attendance));

        // When
        trainingService.unregisterPlayerFromTraining(trainingId, playerId);

        // Then
        verify(attendanceRepository, times(1)).delete(attendance);
    }

    @Test
    void shouldThrowAttendanceNotFoundException() {
        // Given
        Long playerId = 1L;
        Long trainingId = 1L;
        Training training = new Training();

        when(trainingRepository.findById(trainingId)).thenReturn(Optional.of(training));
        when(attendanceRepository.findByPlayer_IdAndTraining_Id(playerId, trainingId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(AttendanceNotFoundException.class, () -> {
            trainingService.unregisterPlayerFromTraining(trainingId, playerId);
        });

        verify(attendanceRepository, never()).delete(any());
    }

    @Test
    void shouldReturnRegisteredPlayerCount() {
        // Given
        Long trainingId = 1L;
        when(trainingRepository.existsById(trainingId)).thenReturn(true);
        when(attendanceRepository.countByTraining_Id(trainingId)).thenReturn(5L);

        // When
        long count = trainingService.getRegisteredPlayersCount(trainingId);

        // Then
        assertEquals(5L, count);
        verify(attendanceRepository, times(1)).countByTraining_Id(trainingId);
    }

    @Test
    void shouldCloseTrainingSuccessfully() {
        // Given
        Long trainingId = 1L;
        Training training = new Training();
        training.setStatus(TrainingStatus.PLANNED);

        when(trainingRepository.findById(trainingId)).thenReturn(Optional.of(training));
        when(trainingRepository.save(any(Training.class))).thenReturn(training);

        // When
        Training result = trainingService.closeTraining(trainingId);

        // Then
        assertEquals(TrainingStatus.FINISHED, result.getStatus());
        verify(trainingRepository, times(1)).save(training);
    }

    @Test
    void shouldThrowTrainingClosedExceptionWhenRegistering() {
        // Given
        Long playerId = 1L;
        Long trainingId = 1L;
        Player player = new Player();
        Training training = new Training();
        training.setStatus(TrainingStatus.FINISHED);

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
        when(trainingRepository.findById(trainingId)).thenReturn(Optional.of(training));

        // When & Then
        assertThrows(TrainingClosedException.class, () -> {
            trainingService.registerPlayerToTraining(trainingId, playerId);
        });

        verify(attendanceRepository, never()).save(any());
    }

    @Test
    void shouldThrowTrainingClosedExceptionWhenUnregistering() {
        // Given
        Long playerId = 1L;
        Long trainingId = 1L;
        Training training = new Training();
        training.setStatus(TrainingStatus.FINISHED);

        when(trainingRepository.findById(trainingId)).thenReturn(Optional.of(training));

        // When & Then
        assertThrows(TrainingClosedException.class, () -> {
            trainingService.unregisterPlayerFromTraining(trainingId, playerId);
        });

        verify(attendanceRepository, never()).delete(any());
    }
}
