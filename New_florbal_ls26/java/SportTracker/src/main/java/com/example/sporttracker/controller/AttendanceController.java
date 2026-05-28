package com.example.sporttracker.controller;

import com.example.sporttracker.entity.Attendance;
import com.example.sporttracker.repository.AttendanceRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AttendanceController {

    private final AttendanceRepository attendanceRepository;

    public AttendanceController(
            AttendanceRepository attendanceRepository
    ) {
        this.attendanceRepository = attendanceRepository;
    }

    @GetMapping("/attendance")
    public List<Attendance> getAttendance() {

        return attendanceRepository.findAll();
    }
}