package org.example.backend.controller;

import org.example.backend.dto.ExerciseDto;
import org.example.backend.model.Exercise;
import org.example.backend.service.ExerciseService;
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
    public List<Exercise> getAllExercises() {
        return exerciseService.getAllExercises();
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
    public void deleteExercise(@PathVariable String id) {
        exerciseService.deleteExercise(id);
    }
}
