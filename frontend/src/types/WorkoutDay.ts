import type {Exercise} from "./Exercise.ts";
import type {DayOfWeek} from "./DayOfWeek.ts";
import type {WorkoutDayType} from "./WorkoutDayType.ts";

export type WorkoutDay = {
    id: string,
    day: DayOfWeek,
    type: WorkoutDayType,
    targetMuscles: string[],
    exercises: Exercise[];
}