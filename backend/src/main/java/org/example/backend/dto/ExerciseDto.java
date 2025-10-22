package org.example.backend.dto;

import org.example.backend.model.MuscleGroup;

public record ExerciseDto(String name, int sets, int reps, MuscleGroup muscleGroup) {
}
