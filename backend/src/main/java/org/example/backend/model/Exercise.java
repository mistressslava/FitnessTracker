package org.example.backend.model;

import org.example.backend.exception.EmptyExerciseFieldException;

// generated ID,
// User can add the name
// order could be useful in future
public record Exercise(String id, String name, int sets, int reps) {

    /*public Exercise {
        if (name == null || name.isBlank()) throw new EmptyExerciseFieldException("Exercise name is required! Please enter a name.");
    }*/
}
