package org.example.backend.model;

import java.util.List;

public record WorkoutPlan(String id, String title, List<WorkoutDay> days) {

    /*public WorkoutPlan {
        if (title == null || title.isBlank()) throw new IllegalArgumentWorkoutPlanFieldException("Workout plan name is required! Please enter a name.");
        if (days == null || days.size() != 7) {
            throw new EmptyExerciseFieldException("WorkoutPlan must contain exactly 7 days!");
        }
        long unique = days.stream()
                .map(WorkoutDay::day)
                .distinct()
                .count();

        if (unique !=7) {
            throw new EmptyExerciseFieldException("Each day must appear exactly once!");
        }
    }*/
}
