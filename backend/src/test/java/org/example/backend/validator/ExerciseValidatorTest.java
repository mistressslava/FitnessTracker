package org.example.backend.validator;

import org.example.backend.dto.ExerciseDto;
import org.example.backend.exception.EmptyExerciseFieldException;
import org.example.backend.model.MuscleGroup;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExerciseValidatorTest {

    ExerciseValidator validator = new ExerciseValidator();

    @Test
    void shouldThrow_whenNameIsNull() {
        ExerciseDto dto = new ExerciseDto(null, 3, 5, MuscleGroup.ARMS);
        Throwable exception = assertThrows(EmptyExerciseFieldException.class,
                () -> validator.validate(dto));
        assertEquals("Exercise name is required! Please enter a name.", exception.getMessage());
    }

    @Test
    void shouldThrow_whenNameIsBlank() {
        ExerciseDto dto = new ExerciseDto("   ", 3, 5, MuscleGroup.ARMS);
        Throwable exception = assertThrows(EmptyExerciseFieldException.class,
                () -> validator.validate(dto));
        assertEquals("Exercise name is required! Please enter a name.", exception.getMessage());
    }

    @Test
    void shouldThrow_whenSetsLessThanOrEqualZero() {
        ExerciseDto dto = new ExerciseDto("Push Up", 0, 10, MuscleGroup.ARMS);
        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> validator.validate(dto));
        assertEquals("Sets must be > 0", exception.getMessage());
    }

    @Test
    void shouldThrow_whenRepsLessThanOrEqualZero() {
        ExerciseDto dto = new ExerciseDto("Push Up", 3, 0, MuscleGroup.ARMS);
        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> validator.validate(dto));
        assertEquals("Reps must be > 0", exception.getMessage());
    }

    @Test
    void shouldNotThrow_whenDtoIsValid() {
        ExerciseDto dto = new ExerciseDto("Push Up", 3, 10, MuscleGroup.ARMS);
        assertDoesNotThrow(() -> validator.validate(dto));
    }

}