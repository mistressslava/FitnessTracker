package org.example.backend.service;

import org.example.backend.dto.ExerciseDto;
import org.example.backend.model.Exercise;
import org.example.backend.repo.ExerciseRepo;
import org.example.backend.validator.ExerciseValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

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
        ExerciseValidator validator = mock(ExerciseValidator.class);
        exerciseService = new ExerciseService(idService, exerciseRepo, validator);
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
    void updateExerciseById_shouldReturnUpdatedExercise_whenCalled() {
        //GIVEN
        Exercise existing = new Exercise("5", "test", 3, 5);
        ExerciseDto updated = new ExerciseDto("Updated Name", 3, 5);

        when(exerciseRepo.findById("5")).thenReturn(Optional.of(existing));
        when(exerciseRepo.save(any(Exercise.class))).thenAnswer(i -> i.getArgument(0));

        //WHEN
        Exercise updatedExercise = exerciseService.updateExerciseById(existing.id(), updated);

        //THEN
        assertEquals("5", updatedExercise.id());
        assertEquals("Updated Name", updatedExercise.name());
        assertEquals(3, updatedExercise.sets());
        assertEquals(5, updatedExercise.reps());

        verify(exerciseRepo).findById("5");
        verify(exerciseRepo).save(updatedExercise);
        verifyNoMoreInteractions(exerciseRepo);
    }

    @Test
    void updateExerciseById_shouldThrowException_whenIdNotFound() {
        //GIVEN
        ExerciseDto updated = new ExerciseDto("Updated Name", 3, 5);
        when(exerciseRepo.findById("5")).thenReturn(Optional.empty());
        //WHEN //THEN
        assertThrows(ResponseStatusException.class,
                () -> exerciseService.updateExerciseById("5", updated));

        verify(exerciseRepo).findById("5");
        verifyNoMoreInteractions(exerciseRepo);
    }

    @Test
    void deleteExercise_shouldDeleteExerciseById_whenIdFound() {
        //GIVEN
        Exercise exercise = new Exercise("1", "TestName", 3, 8);

        when(exerciseRepo.findById("1")).thenReturn(Optional.of(exercise));

        //WHEN
        exerciseService.deleteExercise("1");

        //THEN
        verify(exerciseRepo).deleteById("1");
    }

    @Test
    void deleteExercise_shouldThrowException_whenIdNotFound() {
        //GIVEN
        when(exerciseRepo.findById("2")).thenReturn(Optional.empty());

        //WHEN //THEN
        assertThrows(ResponseStatusException.class,
                () -> exerciseService.deleteExercise("2"));

        verify(exerciseRepo).findById("2");

        verifyNoMoreInteractions(exerciseRepo);
    }
}