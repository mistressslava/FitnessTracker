package org.example.backend.model;

import lombok.Getter;

import java.util.EnumSet;
import java.util.Set;

@Getter
public enum MuscleGroup {
    CHEST(EnumSet.of(WorkoutDayType.UPPER_BODY, WorkoutDayType.FULL_BODY)),
    BACK(EnumSet.of(WorkoutDayType.UPPER_BODY, WorkoutDayType.FULL_BODY)),
    SHOULDERS(EnumSet.of(WorkoutDayType.UPPER_BODY, WorkoutDayType.FULL_BODY)),
    ARMS(EnumSet.of(WorkoutDayType.UPPER_BODY, WorkoutDayType.FULL_BODY)),

    LEGS(EnumSet.of(WorkoutDayType.LOWER_BODY, WorkoutDayType.FULL_BODY)),
    GLUTES(EnumSet.of(WorkoutDayType.LOWER_BODY, WorkoutDayType.FULL_BODY)),
    CALVES(EnumSet.of(WorkoutDayType.LOWER_BODY, WorkoutDayType.FULL_BODY)),

    CORE(EnumSet.of(WorkoutDayType.FULL_BODY)),
    CARDIO(EnumSet.of(WorkoutDayType.FULL_BODY)),
    MOBILITY(EnumSet.of(WorkoutDayType.FULL_BODY));

    private final Set<WorkoutDayType> categories;

    MuscleGroup(Set<WorkoutDayType> categories) {
        this.categories = categories;
    }

}
