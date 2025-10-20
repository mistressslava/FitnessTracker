package org.example.backend.controller;

import org.example.backend.dto.ExerciseDto;
import org.example.backend.model.Exercise;
import org.example.backend.service.ExerciseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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
    public ResponseEntity<Exercise> addNewExercise(@RequestBody ExerciseDto exercise) {
        Exercise newExercise = exerciseService.addNewExercise(exercise);
        return ResponseEntity.created(URI.create("/api/exercises/" + newExercise.id()))
                .body(newExercise);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Exercise> updateExerciseById(@PathVariable String id, @RequestBody ExerciseDto exercise) {
        Exercise updatedExercise = exerciseService.updateExerciseById(id, exercise);
        return ResponseEntity.ok(updatedExercise);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExercise(@PathVariable String id) {
        exerciseService.deleteExercise(id);
        return ResponseEntity.noContent().build(); //204
    }
}
