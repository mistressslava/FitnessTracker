package org.example.backend.model;

import java.util.List;

public record WorkoutDay(WorkoutDayType type, List<Exercise> exercises) {
}
