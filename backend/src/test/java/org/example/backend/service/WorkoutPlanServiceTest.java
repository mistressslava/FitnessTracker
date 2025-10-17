package org.example.backend.service;

import org.example.backend.model.*;
import org.example.backend.repo.WorkoutPlanRepo;
import org.example.backend.util.TestFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class WorkoutPlanServiceTest {

    private WorkoutPlanRepo workoutPlanRepo;
    private WorkoutPlanService workoutPlanService;

    @BeforeEach
    void setup() {
        workoutPlanRepo = mock(WorkoutPlanRepo.class);
        workoutPlanService = new WorkoutPlanService(workoutPlanRepo);
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
}