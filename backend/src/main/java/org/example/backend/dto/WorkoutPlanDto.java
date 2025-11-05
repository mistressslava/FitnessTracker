package org.example.backend.dto;


import java.util.List;

public record WorkoutPlanDto(String title,
                             String description,
                             List<WorkoutDayDto> days) {
}
