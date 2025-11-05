package org.example.backend.controller;

import org.example.backend.dto.WorkoutPlanDto;
import org.example.backend.model.WorkoutPlan;
import org.example.backend.service.WorkoutPlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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

    @GetMapping("/{id}")
    public WorkoutPlan getWorkoutPlanById(@PathVariable String id) {
        return workoutPlanService.getWorkoutPlanById(id);
    }

    @PostMapping
    public ResponseEntity<WorkoutPlan> addNewWorkoutPLan(@RequestBody WorkoutPlanDto workoutPlanDto) {
        WorkoutPlan newPlan = workoutPlanService.addNewWorkoutPlan(workoutPlanDto);
        return ResponseEntity.created(URI.create("/api/workout-plans/" + newPlan.id()))
                .body(newPlan);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkoutPlan> updateWorkoutPlanById(@PathVariable String id, @RequestBody WorkoutPlanDto workoutPlanDto) {
        WorkoutPlan updatedWorkoutPlan = workoutPlanService.updateWorkoutPlanById(id, workoutPlanDto);
        return ResponseEntity.ok(updatedWorkoutPlan);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkoutPlanById(@PathVariable String id) {
        workoutPlanService.deleteWorkoutPlanById(id);
        return ResponseEntity.noContent().build();
    }
}
