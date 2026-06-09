package com.example.sporttracker.controller;

import com.example.sporttracker.entity.Attendance;
import com.example.sporttracker.service.AttendanceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(
            AttendanceService attendanceService
    ) {
        this.attendanceService = attendanceService;
    }

    @GetMapping("/attendance")
    public List<Attendance> getAttendance() {

        return attendanceService.getAttendance();
    }

    @PutMapping("/attendance/{id}/actual")
    public Attendance recordActualAttendance(@PathVariable Long id) {
        return attendanceService.recordActualAttendance(id);
    }
}