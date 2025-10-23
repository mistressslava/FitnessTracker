package org.example.backend.service;

import org.example.backend.dto.WorkoutPlanDto;
import org.example.backend.model.WorkoutPlan;
import org.example.backend.repo.WorkoutPlanRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkoutPlanService {

    private final WorkoutPlanRepo workoutPlanRepo;
    private final IdService idService;

    public WorkoutPlanService(WorkoutPlanRepo workoutPlanRepo, IdService idService) {
        this.workoutPlanRepo = workoutPlanRepo;
        this.idService = idService;
    }

    public List<WorkoutPlan> getAllWorkoutPlans() {
        return workoutPlanRepo.findAll();
    }

    public WorkoutPlan addNewWorkoutPlan(WorkoutPlanDto workoutPlanDto) {
        WorkoutPlan workoutPlan = new WorkoutPlan(
                idService.randomId(),
                workoutPlanDto.title(),
                workoutPlanDto.description(),
                workoutPlanDto.days());
        return workoutPlanRepo.save(workoutPlan);
    }
}
