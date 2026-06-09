package com.example.sporttracker.service;

import com.example.sporttracker.entity.Attendance;
import com.example.sporttracker.exception.AttendanceNotFoundException;
import com.example.sporttracker.repository.AttendanceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public AttendanceService(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    public List<Attendance> getAttendance() {
        return attendanceRepository.findAll();
    }

    public Attendance recordActualAttendance(Long attendanceId) {
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new AttendanceNotFoundException("Attendance not found with id: " + attendanceId));
        attendance.setActualAttendance(true);
        return attendanceRepository.save(attendance);
    }
}
