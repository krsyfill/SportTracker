package com.example.sporttracker.exception;

public class AttendanceAlreadyExistsException extends RuntimeException {
    public AttendanceAlreadyExistsException(String message) {
        super(message);
    }
}