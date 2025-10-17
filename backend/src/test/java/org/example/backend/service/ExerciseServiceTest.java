package org.example.backend.service;

import org.example.backend.model.Exercise;
import org.example.backend.repo.ExerciseRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExerciseServiceTest {

    private ExerciseRepo exerciseRepo;
    private ExerciseService exerciseService;

    @BeforeEach
    void setup() {
        exerciseRepo = mock(ExerciseRepo.class);
        exerciseService = new ExerciseService(exerciseRepo);
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
}