package org.example.backend.validator;

import org.example.backend.dto.ExerciseDto;
import org.example.backend.exception.EmptyExerciseFieldException;
import org.springframework.stereotype.Service;

@Service
public class ExerciseValidator {
    public void validate(ExerciseDto exercise) {
        if (exercise.name() == null || exercise.name().isBlank()) {
            throw new EmptyExerciseFieldException("Exercise name is required! Please enter a name.");
        }
        if (exercise.sets() <= 0) throw new IllegalArgumentException("Sets must be > 0");
        if (exercise.reps() <= 0) throw new IllegalArgumentException("Reps must be > 0");
    }
}
