package org.example.backend.dto;

import org.example.backend.model.Exercise;
import org.example.backend.model.MuscleGroup;
import org.example.backend.model.WorkoutDayType;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

public record WorkoutDayDto(DayOfWeek day,
                            WorkoutDayType type,
                            Set<MuscleGroup> targetMuscles,
                            List<Exercise> exercises) {
}
