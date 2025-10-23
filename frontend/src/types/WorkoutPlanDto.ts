import type {WorkoutDay} from "./WorkoutDay.ts";

export type WorkoutPlanDto = {
    title: string,
    days: WorkoutDay[];
}