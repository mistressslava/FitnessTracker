package org.example.backend.service;

import org.example.backend.dto.WorkoutPlanDto;
import org.example.backend.model.*;
import org.example.backend.repo.WorkoutPlanRepo;
import org.example.backend.util.TestFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class WorkoutPlanServiceTest {

    private WorkoutPlanRepo workoutPlanRepo;
    private WorkoutPlanService workoutPlanService;
    private IdService idService;

    @BeforeEach
    void setup() {
        workoutPlanRepo = mock(WorkoutPlanRepo.class);
        idService = mock(IdService.class);
        workoutPlanService = new WorkoutPlanService(workoutPlanRepo, idService);
    }

    @Test
    void getAllWorkoutPlans_shouldReturnEmptyList() {
        //GIVEN
        when(workoutPlanRepo.findAll()).thenReturn(Collections.emptyList());

        //WHEN
        List<WorkoutPlan> actualList = workoutPlanService.getAllWorkoutPlans();

        //THEN
        assertTrue(actualList.isEmpty());
        verify(workoutPlanRepo).findAll();
    }

    @Test
    void getAllWorkoutPlans_shouldReturnList() {
        //GIVEN

        when(workoutPlanRepo.findAll()).thenReturn(List.of(
                TestFixtures.weekPlan("1"),
                TestFixtures.weekPlan("2")
        ));
        //WHEN
        List<WorkoutPlan> actualList = workoutPlanService.getAllWorkoutPlans();
        //THEN
        assertEquals(2, actualList.size());
        verify(workoutPlanRepo).findAll();
    }

    @Test
    void addNewWorkoutPlan() {
        List<WorkoutDay> days = List.of(
                new WorkoutDay("1", DayOfWeek.MONDAY,    WorkoutDayType.REST,       Set.of(), List.of()),
                new WorkoutDay("2", DayOfWeek.TUESDAY,   WorkoutDayType.UPPER_BODY, Set.of(MuscleGroup.BACK),
                        List.of(new Exercise("ex-1", "Row", 4, 10, MuscleGroup.BACK))),
                new WorkoutDay("3", DayOfWeek.WEDNESDAY, WorkoutDayType.REST,       Set.of(), List.of()),
                new WorkoutDay("4", DayOfWeek.THURSDAY,  WorkoutDayType.LOWER_BODY, Set.of(MuscleGroup.LEGS),
                        List.of(new Exercise("ex-2", "Squat", 5, 5, MuscleGroup.LEGS))),
                new WorkoutDay("5", DayOfWeek.FRIDAY,    WorkoutDayType.REST,       Set.of(), List.of()),
                new WorkoutDay("6", DayOfWeek.SATURDAY,  WorkoutDayType.FULL_BODY,  Set.of(MuscleGroup.CORE),
                        List.of(new Exercise("ex-3", "Plank", 3, 60, MuscleGroup.CORE))),
                new WorkoutDay("7", DayOfWeek.SUNDAY,    WorkoutDayType.REST,       Set.of(), List.of())
        );

        WorkoutPlan expected = new WorkoutPlan("1", "Test description", "Weekly Split", days);

        WorkoutPlanDto dto = new WorkoutPlanDto("Test description", "Weekly Split", days);

        when(idService.randomId()).thenReturn("1");
        when(workoutPlanRepo.save(expected)).thenReturn(expected);

        WorkoutPlan actual = workoutPlanService.addNewWorkoutPlan(dto);

        verify(workoutPlanRepo).save(expected);
        verify(idService).randomId();
        assertEquals(expected, actual);

    }
}