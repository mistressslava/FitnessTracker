package org.example.backend.service;

import org.example.backend.dto.WorkoutDayDto;
import org.example.backend.model.Exercise;
import org.example.backend.model.MuscleGroup;
import org.example.backend.model.WorkoutDay;
import org.example.backend.model.WorkoutDayType;
import org.example.backend.repo.WorkoutDayRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class WorkoutDayServiceTest {

    private WorkoutDayRepo workoutDayRepo;
    private IdService idService;
    private WorkoutDayService workoutDayService;

    @BeforeEach
    void setup() {
        workoutDayRepo = mock(WorkoutDayRepo.class);
        idService = mock(IdService.class);
        workoutDayService = new WorkoutDayService(workoutDayRepo, idService);
    }

    @Test
    void getAllWorkoutDays_shouldReturnListOfWorkoutDays_whenCalled() {
        Exercise exercise = new Exercise("1", "exercise1", 4, 12, MuscleGroup.BACK);
        WorkoutDay workoutDay = new WorkoutDay(
                "1",
                DayOfWeek.FRIDAY,
                WorkoutDayType.UPPER_BODY,
                Set.of(MuscleGroup.BACK),
                List.of(exercise))
        ;

        List<WorkoutDay> expected = List.of(workoutDay);

        when(workoutDayRepo.findAll()).thenReturn(expected);
        List<WorkoutDay> actual = workoutDayService.getAllWorkoutDays();

        verify(workoutDayRepo).findAll();
        assertEquals(expected, actual);
    }

    @Test
    void getAllWorkoutDays_shouldReturnEmptyList_whenCalled() {
        List<WorkoutDay> expected = List.of();

        when(workoutDayRepo.findAll()).thenReturn(expected);
        List<WorkoutDay> actual = workoutDayService.getAllWorkoutDays();

        verify(workoutDayRepo).findAll();
        assertEquals(expected, actual);
    }

    @Test
    void addWorkoutDay_shouldReturnAddedWorkout_whenWorkoutAdded() {
        Exercise exercise = new Exercise("1", "exercise1", 4, 12, MuscleGroup.CORE);
        WorkoutDay expected = new WorkoutDay(
                "1",
                DayOfWeek.FRIDAY,
                WorkoutDayType.FULL_BODY,
                Set.of(MuscleGroup.CORE),
                List.of(exercise));

        WorkoutDayDto workoutDayDto = new WorkoutDayDto(
                DayOfWeek.FRIDAY,
                WorkoutDayType.FULL_BODY,
                Set.of(MuscleGroup.CORE),
                List.of(exercise));

        when(idService.randomId()).thenReturn("1");
        when(workoutDayRepo.save(expected)).thenReturn(expected);
        WorkoutDay actual = workoutDayService.addWorkoutDay(workoutDayDto);

        verify(workoutDayRepo).save(expected);
        verify(idService).randomId();
        assertEquals(expected, actual);
    }

    @Test
    void deleteExercise_shouldDeleteExerciseById_whenIdFound() {
        //GIVEN
        Exercise exercise = new Exercise("1", "exercise1", 4, 12, MuscleGroup.ARMS);
        WorkoutDay expected = new WorkoutDay(
                "1",
                DayOfWeek.FRIDAY,
                WorkoutDayType.FULL_BODY,
                Set.of(MuscleGroup.ARMS),
                List.of(exercise));

        when(workoutDayRepo.findById("1")).thenReturn(Optional.of(expected));

        //WHEN
        workoutDayService.deleteWorkoutDay("1");

        //THEN
        verify(workoutDayRepo).deleteById("1");
    }

    @Test
    void deleteExercise_shouldThrowException_whenIdNotFound() {
        //GIVEN
        when(workoutDayRepo.findById("2")).thenReturn(Optional.empty());

        //WHEN //THEN
        assertThrows(NoSuchElementException.class,
                () -> workoutDayService.deleteWorkoutDay("2"));

        verify(workoutDayRepo).findById("2");

        verifyNoMoreInteractions(workoutDayRepo);
    }
}