package org.example.backend.planGenerator;

import org.example.backend.model.WorkoutPlan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/generate-plan")
public class WorkoutPlanGenerationController {

    private record GeneratePlanRequest(String prompt) {}

    private final WorkoutPlanGenerationService service;

    public WorkoutPlanGenerationController(WorkoutPlanGenerationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<WorkoutPlan> generatePlan(@RequestBody GeneratePlanRequest request) {
        if (request == null || request.prompt() == null) {
            throw new IllegalArgumentException("Prompt is missing or invalid JSON");
        }

        WorkoutPlan plan = service.generatePlanAndSave(request.prompt());
        return ResponseEntity.ok(plan);
    }
}
