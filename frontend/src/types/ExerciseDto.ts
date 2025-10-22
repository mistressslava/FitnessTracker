import type {MuscleGroup} from "./MuscleGroup.ts";

export type ExerciseDto = {
    name: string;
    sets: number;
    reps: number;
    muscleGroup?: MuscleGroup
};