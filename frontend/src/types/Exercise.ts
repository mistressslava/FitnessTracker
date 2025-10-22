import type {MuscleGroup} from "./MuscleGroup.ts";

export type Exercise = {
    id: string,
    name: string,
    sets: number,
    reps: number,
    muscleGroup: MuscleGroup
}