package org.example.backend.util;

import org.example.backend.model.*;

import java.time.DayOfWeek;
import java.util.*;

public final class TestFixtures {

    private TestFixtures() {
    }

    public static WorkoutPlan weekPlan(String id) {
        return new WorkoutPlan(id, "Week " + id, weekDays());
    }

    public static List<WorkoutDay> weekDays() {
        return List.of(
                day("1", DayOfWeek.MONDAY,    WorkoutDayType.REST),
                day("2", DayOfWeek.TUESDAY,   WorkoutDayType.UPPER_BODY, MuscleGroup.BACK),
                day("3", DayOfWeek.WEDNESDAY, WorkoutDayType.REST),
                day("4", DayOfWeek.THURSDAY,  WorkoutDayType.LOWER_BODY, MuscleGroup.LEGS),
                day("5", DayOfWeek.FRIDAY,    WorkoutDayType.REST),
                day("6", DayOfWeek.SATURDAY,  WorkoutDayType.FULL_BODY,  MuscleGroup.CORE),
                day("7", DayOfWeek.SUNDAY,    WorkoutDayType.REST)
        );
    }

    private static WorkoutDay day(String i, DayOfWeek dow, WorkoutDayType type, MuscleGroup... groups) {
        Set<MuscleGroup> targets = (groups.length == 0)
                ? Set.of()
                : EnumSet.copyOf(Arrays.asList(groups));

        List<Exercise> exercises = (type == WorkoutDayType.REST)
                ? List.of(new Exercise("rest-" + i, "Rest", 0, 0, MuscleGroup.ARMS))
                : List.of(new Exercise("ex-" + i, "Exercise-" + i, 3, 10, MuscleGroup.ARMS));

        return new WorkoutDay("day-" + i, dow, type, targets, exercises);
    }
}
