package org.example.backend.validator;

import org.example.backend.dto.ExerciseDto;
import org.example.backend.exception.EmptyExerciseFieldException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExerciseValidatorTest {

    ExerciseValidator validator = new ExerciseValidator();

    @Test
    void shouldThrow_whenNameIsNull() {
        ExerciseDto dto = new ExerciseDto(null, 3, 5);
        assertThrows(EmptyExerciseFieldException.class, () -> validator.validate(dto));
    }

    @Test
    void shouldThrow_whenNameIsBlank() {
        ExerciseDto dto = new ExerciseDto("   ", 3, 5);
        assertThrows(EmptyExerciseFieldException.class, () -> validator.validate(dto));
    }

    @Test
    void shouldThrow_whenSetsLessThanOrEqualZero() {
        ExerciseDto dto = new ExerciseDto("Push Up", 0, 10);
        assertThrows(IllegalArgumentException.class, () -> validator.validate(dto));
    }

    @Test
    void shouldThrow_whenRepsLessThanOrEqualZero() {
        ExerciseDto dto = new ExerciseDto("Push Up", 3, 0);
        assertThrows(IllegalArgumentException.class, () -> validator.validate(dto));
    }

    @Test
    void shouldNotThrow_whenDtoIsValid() {
        ExerciseDto dto = new ExerciseDto("Push Up", 3, 10);
        assertDoesNotThrow(() -> validator.validate(dto));
    }

}