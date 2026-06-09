package com.example.sporttracker.exception;

public class TrainingClosedException extends RuntimeException {
    public TrainingClosedException(String message) {
        super(message);
    }
}
