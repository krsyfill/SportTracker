package com.example.sporttracker.exception;

public class AttendanceNotFoundException extends RuntimeException {
    public AttendanceNotFoundException(String message) {
        super(message);
    }
}
