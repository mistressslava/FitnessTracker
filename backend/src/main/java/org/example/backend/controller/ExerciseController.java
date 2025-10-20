package org.example.backend.controller;

import org.example.backend.dto.ExerciseDto;
import org.example.backend.model.Exercise;
import org.example.backend.service.ExerciseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {

    private final ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @GetMapping
    public ResponseEntity<List<Exercise>> getAllExercises() {
        List<Exercise> exerciseList = exerciseService.getAllExercises();
        if (exerciseList.isEmpty()) {
            return ResponseEntity.noContent().build(); //204
        }
        return ResponseEntity.ok(exerciseList);
    }

    @PostMapping
    public Exercise addNewExercise(@RequestBody ExerciseDto exercise) {
        return exerciseService.addNewExercise(exercise);
    }

    @PutMapping("/{id}")
    public Exercise updateExerciseById(@PathVariable String id, @RequestBody ExerciseDto exercise) {
        return exerciseService.updateExerciseById(id, exercise);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExercise(@PathVariable String id) {
        exerciseService.deleteExercise(id);
        return ResponseEntity.noContent().build(); //204
    }
}
