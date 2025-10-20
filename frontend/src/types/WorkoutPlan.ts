import type {WorkoutDay} from "./WorkoutDay.ts";

export type WorkoutPlan = {
    id: string,
    title: string,
    days: WorkoutDay[];
}