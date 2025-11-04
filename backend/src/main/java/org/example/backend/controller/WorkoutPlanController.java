package org.example.backend.controller;

import org.example.backend.dto.WorkoutPlanDto;
import org.example.backend.model.WorkoutPlan;
import org.example.backend.service.WorkoutPlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workout-plans")
public class WorkoutPlanController {

    private final WorkoutPlanService workoutPlanService;

    public WorkoutPlanController(WorkoutPlanService workoutPlanService) {
        this.workoutPlanService = workoutPlanService;
    }

    @GetMapping
    public List<WorkoutPlan> getAllWorkoutPlans() {
        return workoutPlanService.getAllWorkoutPlans();
    }

    @PostMapping
    public WorkoutPlan addNewWorkoutPLan(@RequestBody WorkoutPlanDto workoutPlanDto) {
        return workoutPlanService.addNewWorkoutPlan(workoutPlanDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkoutPlan> updateWorkoutPlanById(@PathVariable String id, @RequestBody WorkoutPlanDto workoutPlanDto) {
        WorkoutPlan updatedWorkoutPlan = workoutPlanService.updateWorkoutPlanById(id, workoutPlanDto);
        return ResponseEntity.ok(updatedWorkoutPlan);
    }
}
