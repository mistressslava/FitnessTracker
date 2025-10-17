package org.example.backend.service;

import org.example.backend.dto.ExerciseDto;
import org.example.backend.exception.EmptyExerciseFieldException;
import org.example.backend.model.Exercise;
import org.example.backend.repo.ExerciseRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExerciseService {

    private final ExerciseRepo exerciseRepo;
    private final IdService idService;

    public ExerciseService(IdService idService, ExerciseRepo exerciseRepo) {
        this.idService = idService;
        this.exerciseRepo = exerciseRepo;
    }

    public List<Exercise> getAllExercises() {
        return exerciseRepo.findAll();
    }

    public Exercise addNewExercise(ExerciseDto exercise) {
        if (exercise.name() == null || exercise.name().isBlank()) {
            throw new EmptyExerciseFieldException("Exercise name is required! Please enter a name.");
        }
        if (exercise.sets() <= 0) throw new IllegalArgumentException("Sets must be > 0");
        if (exercise.reps() <= 0) throw new IllegalArgumentException("Reps must be > 0");
        Exercise newExercise = new Exercise(idService.randomId(), exercise.name(), exercise.sets(), exercise.reps());
        return exerciseRepo.save(newExercise);
    }
}
