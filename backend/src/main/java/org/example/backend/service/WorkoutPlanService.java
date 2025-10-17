package org.example.backend.service;

import org.example.backend.model.WorkoutPlan;
import org.example.backend.repo.WorkoutPlanRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkoutPlanService {

    private final WorkoutPlanRepo workoutPlanRepo;

    public WorkoutPlanService(WorkoutPlanRepo workoutPlanRepo) {
        this.workoutPlanRepo = workoutPlanRepo;
    }

    public List<WorkoutPlan> getAllWorkoutPlans() {
        return workoutPlanRepo.findAll();
    }
}
