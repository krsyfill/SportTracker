package com.example.sporttracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PlayerNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handlePlayerNotFoundException(PlayerNotFoundException ex) {
        return createErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(TrainingNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleTrainingNotFoundException(TrainingNotFoundException ex) {
        return createErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(AttendanceAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleAttendanceAlreadyExistsException(AttendanceAlreadyExistsException ex) {
        return createErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(AttendanceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleAttendanceNotFoundException(AttendanceNotFoundException ex) {
        return createErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(TrainingClosedException.class)
    public ResponseEntity<Map<String, Object>> handleTrainingClosedException(TrainingClosedException ex) {
        return createErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handlePaymentNotFoundException(PaymentNotFoundException ex) {
        return createErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    private ResponseEntity<Map<String, Object>> createErrorResponse(HttpStatus status, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", status.value());
        response.put("message", message);
        return new ResponseEntity<>(response, status);
    }
}