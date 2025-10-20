package org.example.backend.service;

import org.example.backend.dto.ExerciseDto;
import org.example.backend.exception.EmptyExerciseFieldException;
import org.example.backend.model.Exercise;
import org.example.backend.repo.ExerciseRepo;
import org.example.backend.validator.ExerciseValidator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ExerciseService {

    private final ExerciseRepo exerciseRepo;
    private final IdService idService;
    private final ExerciseValidator validator;

    private String buildExerciseNotFoundMessage(String id) {
        return "Exercise with id " + id + " not found";
    }

    public ExerciseService(IdService idService, ExerciseRepo exerciseRepo, ExerciseValidator validator) {
        this.idService = idService;
        this.exerciseRepo = exerciseRepo;
        this.validator = validator;
    }

    public List<Exercise> getAllExercises() {
        return exerciseRepo.findAll();
    }

    public Exercise addNewExercise(ExerciseDto exercise) {
        validator.validate(exercise);

        Exercise newExercise = new Exercise(idService.randomId(), exercise.name(), exercise.sets(), exercise.reps());
        return exerciseRepo.save(newExercise);
    }

    public Exercise updateExerciseById(String id, ExerciseDto exercise) {
        Exercise existing = exerciseRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, buildExerciseNotFoundMessage(id)));

        validator.validate(exercise);

        Exercise updated = new Exercise(existing.id(), exercise.name(), exercise.sets(), exercise.reps());
        return exerciseRepo.save(updated);
    }

    public void deleteExercise(String id) {
        Exercise existing = exerciseRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, buildExerciseNotFoundMessage(id)));
        exerciseRepo.deleteById(existing.id());
    }
}
