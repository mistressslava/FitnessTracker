import type {DayOfWeek} from "./DayOfWeek.ts";
import type {WorkoutDayType} from "./WorkoutDayType.ts";
import type {MuscleGroup} from "./MuscleGroup.ts";
import type {ExerciseDto} from "./ExerciseDto.ts";

export type WorkoutDayDto = {
    day: DayOfWeek,
    type: WorkoutDayType,
    targetMuscles: MuscleGroup[],
    exercises: ExerciseDto[];
}