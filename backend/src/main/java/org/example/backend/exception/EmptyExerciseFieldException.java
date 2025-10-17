package org.example.backend.exception;

public class EmptyExerciseFieldException extends RuntimeException {
    public EmptyExerciseFieldException(String message) {
        super(message);
    }
}
