package org.example.backend.dto;

import org.example.backend.model.WorkoutDay;

import java.util.List;

public record WorkoutPlanDto(String title, List<WorkoutDay> days) {
}
