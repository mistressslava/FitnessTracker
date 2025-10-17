package org.example.backend.exception;

public class IllegalArgumentWorkoutPlanFieldException extends RuntimeException {
    public IllegalArgumentWorkoutPlanFieldException(String message) {
        super(message);
    }
}
