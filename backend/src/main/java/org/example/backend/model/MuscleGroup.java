package org.example.backend.model;

import lombok.Getter;

@Getter
public enum MuscleGroup {
    CHEST(WorkoutDayType.UPPER_BODY),
    BACK(WorkoutDayType.UPPER_BODY),
    SHOULDERS(WorkoutDayType.UPPER_BODY),
    ARMS(WorkoutDayType.UPPER_BODY),

    LEGS(WorkoutDayType.LOWER_BODY),
    GLUTES(WorkoutDayType.LOWER_BODY),
    CALVES(WorkoutDayType.LOWER_BODY),

    CORE(WorkoutDayType.FULL_BODY),
    CARDIO(WorkoutDayType.FULL_BODY),
    MOBILITY(WorkoutDayType.FULL_BODY);

    private final WorkoutDayType category;

    MuscleGroup(WorkoutDayType category) {
        this.category = category;
    }

}
