package com.example.sporttracker.service;

import com.example.sporttracker.entity.Attendance;
import com.example.sporttracker.exception.AttendanceNotFoundException;
import com.example.sporttracker.repository.AttendanceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AttendanceServiceTest {

    @Mock
    private AttendanceRepository attendanceRepository;

    @InjectMocks
    private AttendanceService attendanceService;

    @Test
    void shouldRecordActualAttendance() {
        // Given
        Long attendanceId = 1L;
        Attendance attendance = new Attendance();
        attendance.setId(attendanceId);
        attendance.setActualAttendance(false);

        when(attendanceRepository.findById(attendanceId)).thenReturn(Optional.of(attendance));
        when(attendanceRepository.save(any(Attendance.class))).thenReturn(attendance);

        // When
        Attendance result = attendanceService.recordActualAttendance(attendanceId);

        // Then
        assertTrue(result.getActualAttendance());
        verify(attendanceRepository, times(1)).save(attendance);
    }

    @Test
    void shouldThrowAttendanceNotFoundException() {
        // Given
        Long attendanceId = 1L;
        when(attendanceRepository.findById(attendanceId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(AttendanceNotFoundException.class, () -> {
            attendanceService.recordActualAttendance(attendanceId);
        });

        verify(attendanceRepository, never()).save(any());
    }
}
