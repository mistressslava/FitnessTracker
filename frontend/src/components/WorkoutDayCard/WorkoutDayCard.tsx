import type {Exercise} from "@/types/Exercise.ts";
import {CardContent} from "@/components/ui/card.tsx";

type ExerciseListProps = {
    exercises: Exercise[];
    isRestDay?: boolean;
}

export default function WorkoutDayCard(props: Readonly<ExerciseListProps>) {

    if (props.isRestDay) {
        return (
            <CardContent className="pt-4">
                <h3 className="text-center text-sm text-muted-foreground">Rest Day</h3>
            </CardContent>
        )
    }

    if(!props.exercises?.length) return null;

    return (
        <CardContent className="pt-4">
            <div className="space-y-4">
                {props.exercises.map(exercise => (
                    <div
                        key={exercise.id}
                        className="flex items-start justify-between gap-4 pb-4 border-b last:border-b-0 last:pb-0"
                    >
                        <div className="flex-1 text-left">
                            <h3 className="font-semibold text-sm leading-relaxed">
                                {exercise.name}
                            </h3>
                        </div>

                        {exercise.sets > 0 && exercise.reps > 0 && (
                            <div className="flex gap-3 text-sm text-muted-foreground shrink-0">
                                <div className="text-center">
                                    <div className="font-semibold text-foreground">{exercise.sets}</div>
                                    <div className="text-xs">sets</div>
                                </div>
                                <div className="text-muted-foreground">×</div>
                                <div className="text-center">
                                    <div className="font-semibold text-foreground">{exercise.reps}</div>
                                    <div className="text-xs">reps</div>
                                </div>
                            </div>
                        )}
                    </div>
                ))}
            </div>
        </CardContent>
    )
}