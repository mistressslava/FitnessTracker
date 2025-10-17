package org.example.backend.service;

import org.example.backend.model.WorkoutPlan;
import org.example.backend.repo.WorkoutPlanRepo;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class WorkoutPlanServiceTest {

    private WorkoutPlanRepo workoutPlanRepo = mock(WorkoutPlanRepo.class);
    private WorkoutPlanService workoutPlanService = mock(WorkoutPlanService.class);

    @Test
    void getAllWorkoutPlans_shouldReturnEmptyList() {
        //GIVEN
        List<WorkoutPlan> expectedList = List.of();

        when(workoutPlanRepo.findAll()).thenReturn(expectedList);
        //WHEN
        List<WorkoutPlan> actualList = workoutPlanService.getAllWorkoutPlans();
        //THEN
        assertEquals(expectedList, actualList);
    }

    @Test
    void getAllWorkoutPlans_shouldReturnList() {
        //GIVEN
        WorkoutPlan plan1 = new WorkoutPlan("1", "");
        List<WorkoutPlan> expectedList = List.of();

        when(workoutPlanRepo.findAll()).thenReturn(expectedList);
        //WHEN
        List<WorkoutPlan> actualList = workoutPlanService.getAllWorkoutPlans();
        //THEN
        assertEquals(expectedList, actualList);
    }
}