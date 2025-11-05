package org.example.backend.service;

import org.example.backend.dto.ExerciseDto;
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
import static org.junit.jupiter.api.Assertions.*;
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
        // GIVEN
        WorkoutDayDto dto = new WorkoutDayDto(
                DayOfWeek.FRIDAY,
                WorkoutDayType.FULL_BODY,
                Set.of(MuscleGroup.CORE),
                List.of(new ExerciseDto("exercise1", 4, 12, MuscleGroup.CORE))
        );

        when(idService.randomId()).thenReturn("day-1", "ex-1");
        when(workoutDayRepo.save(any(WorkoutDay.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        // WHEN
        WorkoutDay actual = workoutDayService.addWorkoutDay(dto);

        // THEN
        assertEquals("ex-1", actual.id());
        assertEquals(DayOfWeek.FRIDAY, actual.day());
        assertEquals(WorkoutDayType.FULL_BODY, actual.type());
        assertTrue(actual.targetMuscles().contains(MuscleGroup.CORE));

        assertEquals(1, actual.exercises().size());
        Exercise ex = actual.exercises().getFirst();
        assertEquals("day-1", ex.id());
        assertEquals("exercise1", ex.name());
        assertEquals(4, ex.sets());
        assertEquals(12, ex.reps());
        assertEquals(MuscleGroup.CORE, ex.muscleGroup());

        verify(idService, times(2)).randomId();
        verify(workoutDayRepo).save(any(WorkoutDay.class));
        verifyNoMoreInteractions(workoutDayRepo);
    }

    @Test
    void updateWorkoutDayById_shouldThrow_whenNotFound() {
        // GIVEN
        WorkoutDayDto dto = new WorkoutDayDto(
                DayOfWeek.MONDAY,
                WorkoutDayType.REST,
                Set.of(),
                List.of()
        );
        when(workoutDayRepo.findById("missing")).thenReturn(Optional.empty());

        // WHEN / THEN
        assertThrows(NoSuchElementException.class,
                () -> workoutDayService.updateWorkoutDayById("missing", dto));

        verify(workoutDayRepo).findById("missing");
        verifyNoMoreInteractions(workoutDayRepo);
    }

    @Test
    void updateWorkoutDayById_shouldUpdateFields_andKeepId() {
        //GIVEN
        WorkoutDay existing = new WorkoutDay(
                "day-1",
                DayOfWeek.FRIDAY,
                WorkoutDayType.FULL_BODY,
                Set.of(MuscleGroup.CORE),
                List.of(new Exercise("ex-old", "Old", 3, 10, MuscleGroup.CORE))
        );

        WorkoutDayDto dto = new WorkoutDayDto(
                DayOfWeek.FRIDAY,
                WorkoutDayType.LOWER_BODY,
                Set.of(MuscleGroup.LEGS),
                List.of(
                        new ExerciseDto("Squat", 5, 5, MuscleGroup.LEGS),
                        new ExerciseDto("Lunge", 3, 12, MuscleGroup.GLUTES)
                )
        );

        when(workoutDayRepo.findById("day-1")).thenReturn(Optional.of(existing));
        when(idService.randomId()).thenReturn("ex-1", "ex-2");
        when(workoutDayRepo.save(any(WorkoutDay.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        // WHEN
        WorkoutDay actual = workoutDayService.updateWorkoutDayById("day-1", dto);

        // THEN
        assertEquals("day-1", actual.id());
        assertEquals(DayOfWeek.FRIDAY, actual.day());
        assertEquals(WorkoutDayType.LOWER_BODY, actual.type());

        assertEquals(2, actual.exercises().size());
        assertEquals("ex-1", actual.exercises().get(0).id());
        assertEquals("Squat", actual.exercises().get(0).name());
        assertEquals("ex-2", actual.exercises().get(1).id());
        assertEquals("Lunge", actual.exercises().get(1).name());

        assertTrue(actual.targetMuscles().contains(MuscleGroup.LEGS));

        verify(workoutDayRepo).findById("day-1");
        verify(idService, times(2)).randomId();
        verify(workoutDayRepo).save(any(WorkoutDay.class));
        verifyNoMoreInteractions(workoutDayRepo);
    }

    @Test
    void updateWorkoutDayById_restType_shouldClearTargetsAndExercises() {
        // GIVEN day with exercises
        WorkoutDay existing = new WorkoutDay(
                "day-1",
                DayOfWeek.WEDNESDAY,
                WorkoutDayType.FULL_BODY,
                Set.of(MuscleGroup.CHEST),
                List.of(new Exercise("ex-1", "Pushup", 3, 15, MuscleGroup.CHEST))
        );

        WorkoutDayDto dto = new WorkoutDayDto(
                DayOfWeek.WEDNESDAY,
                WorkoutDayType.REST,
                Set.of(MuscleGroup.CHEST),
                List.of(new ExerciseDto("Foo", 1, 1, MuscleGroup.CHEST))
        );

        when(workoutDayRepo.findById("day-1")).thenReturn(Optional.of(existing));
        when(workoutDayRepo.save(any(WorkoutDay.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        // WHEN
        WorkoutDay actual = workoutDayService.updateWorkoutDayById("day-1", dto);

        // THEN
        assertEquals("day-1", actual.id());
        assertEquals(DayOfWeek.WEDNESDAY, actual.day());
        assertEquals(WorkoutDayType.REST, actual.type());
        assertTrue(actual.exercises().isEmpty());
        assertTrue(actual.targetMuscles().isEmpty());

        verify(workoutDayRepo).findById("day-1");
        verify(workoutDayRepo).save(any(WorkoutDay.class));
        verifyNoMoreInteractions(workoutDayRepo);
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