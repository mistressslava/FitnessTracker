package org.example.backend.dto;

import org.example.backend.model.WorkoutDay;

import java.util.List;

public record WorkoutPlanDto(String title,
                             String description,
                             List<WorkoutDay> days) {
}
