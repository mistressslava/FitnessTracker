package org.example.backend.model;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

public record WorkoutDay(String id, DayOfWeek day, WorkoutDayType type, Set<MuscleGroup> targetMuscles,
                         List<Exercise> exercises) {

   /* public WorkoutDay {
        targetMuscles= targetMuscles == null ? Set.of() : Set.copyOf(targetMuscles);
        exercises = exercises == null ? List.of() : List.copyOf(exercises);

        if (type == WorkoutDayType.REST) {
            targetMuscles = Set.of();
            exercises = List.of();
        } else {
            boolean targetCheck = targetMuscles.stream()
                    .allMatch(muscleGroup -> muscleGroup.getCategory() == type);
            if (!targetCheck) throw new IllegalArgumentException("All target muscles must match " + type);
        }
    }*/
}
