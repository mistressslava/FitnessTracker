import type {MuscleGroup} from "./MuscleGroup.ts";

export type ExerciseDto = {
    id?: string;
    name: string;
    sets: number;
    reps: number;
    muscleGroup?: MuscleGroup
};