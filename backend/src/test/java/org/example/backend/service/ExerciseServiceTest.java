package org.example.backend.service;

import org.example.backend.dto.ExerciseDto;
import org.example.backend.exception.EmptyExerciseFieldException;
import org.example.backend.model.Exercise;
import org.example.backend.repo.ExerciseRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExerciseServiceTest {

    private ExerciseRepo exerciseRepo;
    private IdService idService;
    private ExerciseService exerciseService;

    @BeforeEach
    void setup() {
        exerciseRepo = mock(ExerciseRepo.class);
        idService = mock(IdService.class);
        exerciseService = new ExerciseService(idService, exerciseRepo);
    }

    @Test
    void getAllExercises_shouldReturnListOfExercises_whenCalled() {
        //GIVEN
        Exercise exercise1 = new Exercise("1", "exercise1", 4, 12);
        Exercise exercise2 = new Exercise("2", "exercise2", 4, 8);
        List<Exercise> expected = List.of(exercise1, exercise2);

        //WHEN
        when(exerciseRepo.findAll()).thenReturn(expected);
        List<Exercise> actual = exerciseService.getAllExercises();

        //THEN
        verify(exerciseRepo).findAll();
        assertEquals(expected, actual);
    }

    @Test
    void getAllExercises_shouldReturnEmptyList_whenCalled() {
        //GIVEN
        List<Exercise> expected = List.of();

        //WHEN
        when(exerciseRepo.findAll()).thenReturn(expected);
        List<Exercise> actual = exerciseService.getAllExercises();

        //THEN
        verify(exerciseRepo).findAll();
        assertEquals(expected, actual);
    }

    @Test
    void addNewExercise_shouldReturnAddedExercise_whenExerciseAdded() {
        //GIVEN
        Exercise exercise = new Exercise("Test-id", "exerciseName", 3, 12);
        ExerciseDto exerciseDto = new ExerciseDto("exerciseName", 3, 12);

        //WHEN
        when(idService.randomId()).thenReturn("Test-id");
        when(exerciseRepo.save(exercise)).thenReturn(exercise);
        Exercise actual = exerciseService.addNewExercise(exerciseDto);

        //THEN
        verify(exerciseRepo).save(exercise);
        verify(idService).randomId();
        assertEquals(exercise, actual);
    }

    @Test
    void addNewExercise_shouldThrowException_whenEmptyStringWasAdded() {
        //GIVEN
        ExerciseDto exerciseDto = new ExerciseDto("", 0, 0);

        //THEN
        assertThrows(EmptyExerciseFieldException.class, () -> {
            throw new EmptyExerciseFieldException("Exercise name is required! Please enter a name.");
        });

        Throwable exception = assertThrows(EmptyExerciseFieldException.class,
                () -> exerciseService.addNewExercise(exerciseDto));

        assertEquals("Exercise name is required! Please enter a name.", exception.getMessage());

    }
}