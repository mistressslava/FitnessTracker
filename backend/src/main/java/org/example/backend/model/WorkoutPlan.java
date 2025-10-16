package org.example.backend.model;

import java.util.List;

public record WorkoutPlan(String id, String title, List<WorkoutDay> days) {
}
