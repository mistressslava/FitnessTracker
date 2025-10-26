import type {WorkoutDay} from "./WorkoutDay.ts";

export type WorkoutPlanDto = {
    id?: string,
    title: string,
    description: string,
    days: WorkoutDay[];
}