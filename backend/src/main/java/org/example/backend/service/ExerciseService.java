package org.example.backend.service;

import org.example.backend.model.Exercise;
import org.example.backend.repo.ExerciseRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExerciseService {

    private final ExerciseRepo exerciseRepo;

    public ExerciseService(ExerciseRepo exerciseRepo) {
        this.exerciseRepo = exerciseRepo;
    }

    public List<Exercise> getAllExercises() {
        return exerciseRepo.findAll();
    }
}
