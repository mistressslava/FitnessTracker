import type {WorkoutDay} from "@/types/WorkoutDay.ts";
import {useEffect, useState} from "react";
import type {Exercise} from "@/types/Exercise.ts";
import axios from "axios";
import {Input} from "@/components/ui/input.tsx";
import {MUSCLE_GROUPS, type MuscleGroup} from "@/types/MuscleGroup.ts";
import {Button} from "@/components/ui/button.tsx";

type WorkoutDayProps = {
    workoutDay: WorkoutDay;
    onSaved?: (saved: WorkoutDay) => void;
    onCancel?: () => void;
}

export default function UpdateWorkoutDay(props: Readonly<WorkoutDayProps>) {
    const [draftDay, setDraftDay] = useState(props.workoutDay.day);
    const [draftType, setDraftType] = useState(props.workoutDay.type);
    const [draftTargetMuscle, setDraftTargetMuscle] = useState(props.workoutDay.targetMuscles ?? []);
    const [draftExercises, setDraftExercises] = useState<Exercise[]>(props.workoutDay.exercises ?? []);

    const [isEditing, setIsEditing] = useState(false);

    useEffect(() => {
        setDraftDay(props.workoutDay.day);
        setDraftType(props.workoutDay.type);
        setDraftTargetMuscle(props.workoutDay.targetMuscles ?? []);
        setDraftExercises(props.workoutDay.exercises ?? []);
    }, [props.workoutDay]);

    // UPDATE
    function updateWorkoutDay() {
        axios.put('/api/workout-days/' + props.workoutDay.id, {
            day: draftDay,
            type: draftType,
            targetMuscles: draftTargetMuscle,
            exercises: draftExercises,
        })
            .then(response => {
                setDraftDay(response.data.day);
                setDraftType(response.data.type);
                setDraftTargetMuscle(response.data.targetMuscle ?? []);
                setDraftExercises(response.data.exercises ?? []);
                setIsEditing(false);
                props.onSaved?.(response.data);
            })
            .catch(e => {
                console.log(e);
                alert(String(e.response?.data ?? e.message));
            });
    }

    return (
        <div className="space-y-4">

            <Button
                onClick={() => {
                updateWorkoutDay();
                setIsEditing(!isEditing)
            }}
                className="bg-primary text-primary-foreground hover:bg-primary/90"
            >Save</Button>

            {draftExercises.map((exercise, index) => (
                <div key={props.workoutDay.id}
                     className="flex items-start justify-between gap-4 pb-4 border-b last:border-b-0 last:pb-0">
                    <div key={exercise.id}>
                        <div className="flex-1 text-left">
                            <Input
                                className="font-semibold text-sm leading-relaxed"
                                type="text"
                                value={exercise.name}
                                onChange={e => {
                                    setDraftExercises(prev =>
                                        prev.map((ex, i) =>
                                            i === index ? {...ex, name: e.target.value} : ex
                                        )
                                    )
                                }}
                            />
                        </div>

                        <div className="flex gap-3 text-sm text-muted-foreground shrink-0">
                            <div className="text-center">
                                <Input
                                    className="font-semibold text-foreground w-20"
                                    type="number"
                                    value={exercise.sets}
                                    onChange={e => {
                                        setDraftExercises(prev =>
                                            prev.map((ex, i) =>
                                                i === index ? {...ex, sets: Number(e.target.value)} : ex
                                            ))
                                    }}
                                />
                                <div className="text-xs">sets</div>
                            </div>
                            <div className="text-muted-foreground">×</div>
                            <div className="text-center">
                                <Input
                                    className="font-semibold text-foreground w-20"
                                    type="number"
                                    value={exercise.reps}
                                    onChange={e => {
                                        setDraftExercises(prev =>
                                            prev.map((ex, i) =>
                                                i === index ? {...ex, reps: Number(e.target.value)} : ex
                                            ))
                                    }}
                                />
                                <div className="text-xs">reps</div>
                            </div>
                        </div>

                        <select
                            value={exercise.muscleGroup}
                            onChange={e => {
                                setDraftExercises(prev =>
                                    prev.map((ex, i) =>
                                        i === index ? {...ex, muscleGroup: e.target.value as MuscleGroup} : ex
                                    ))
                            }}
                        >
                            {Object.values(MUSCLE_GROUPS).map(group => (

                                <option key={group} value={group}>
                                    {group}
                                </option>
                            ))
                            }
                        </select>

                    </div>
                </div>
            ))}
        </div>
    )

}