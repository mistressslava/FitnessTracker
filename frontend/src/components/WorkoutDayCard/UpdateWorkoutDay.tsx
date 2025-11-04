import type {WorkoutDay} from "@/types/WorkoutDay.ts";
import {useEffect, useState} from "react";
import type {Exercise} from "@/types/Exercise.ts";
import axios from "axios";
import {Input} from "@/components/ui/input.tsx";
import {MUSCLE_GROUPS, type MuscleGroup} from "@/types/MuscleGroup.ts";
import {Button} from "@/components/ui/button.tsx";
import {Label} from "@/components/ui/label.tsx";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select.tsx";
import {useExercises} from "@/components/ExerciseCard/UseExercises.ts";

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

    const {exercises: library} = useExercises();

    useEffect(() => {
        setDraftDay(props.workoutDay.day);
        setDraftType(props.workoutDay.type);
        setDraftTargetMuscle(props.workoutDay.targetMuscles ?? []);
        setDraftExercises(props.workoutDay.exercises ?? []);
    }, [props.workoutDay]);

    // UPDATE
    function updateWorkoutDay() {

        const draftTargetMuscle = Array.from(
            new Set(
                (draftExercises ?? [])
                    .map(ex => ex.muscleGroup)
                    .filter(Boolean)
            )
        );

        axios.put('/api/workout-days/' + props.workoutDay.id, {
            day: draftDay,
            type: draftType,
            targetMuscles: draftTargetMuscle,
            exercises: draftExercises,
        })
            .then(response => {
                setDraftDay(response.data.day);
                setDraftType(response.data.type);
                setDraftTargetMuscle(response.data.targetMuscles ?? []);
                setDraftExercises(response.data.exercises ?? []);
                setIsEditing(false);
                props.onSaved?.(response.data);
            })
            .catch(e => {
                console.log(e);
                alert(String(e.response?.data ?? e.message));
            });
    }

    //Delete Exercise inside of Workout day
    function handleDelete(id: string) {
        setDraftExercises(prev => prev.filter(ex => ex.id !== id));
    }

    //Add Exercise inside of Workout day
    function addFromLibrary(exId: string) {
        const lib = library.find((x: Exercise) => x.id === exId);
        if (!lib) return;

        if (draftExercises.some(ex => ex.id === lib.id)) {
            return;
        }

        const newExercise: Exercise = {
            id: lib.id,
            name: lib.name,
            sets: lib.sets,
            reps: lib.reps,
            muscleGroup: lib.muscleGroup,
        }

        setDraftExercises(prev => [...prev, newExercise])

        if (lib.muscleGroup && !draftTargetMuscle.includes(lib.muscleGroup)) {
            setDraftTargetMuscle(prev => [...prev, lib.muscleGroup])
        }
    }

    function handleCancel() {
        setDraftDay(props.workoutDay.day)
        setDraftType(props.workoutDay.type)
        setDraftTargetMuscle(props.workoutDay.targetMuscles ?? [])
        setDraftExercises(props.workoutDay.exercises ?? [])
        setIsEditing(false)
        props.onCancel?.()
    }


    return (
        <div className="space-y-4">
            <div className="flex gap-2 justify-center">
                <Button
                    onClick={() => {
                        updateWorkoutDay();
                        setIsEditing(!isEditing)
                    }}
                    className="bg-primary text-primary-foreground px-4 py-2 hover:bg-primary/80"
                >
                    Save
                </Button>
                <Button
                    onClick={handleCancel}
                    className="bg-destructive text-primary-foreground px-4 py-2 hover:bg-destructive/80"
                >
                    Cancel
                </Button>
            </div>

            {draftExercises.map((exercise, index) => (
                <div key={exercise.id}
                     className="flex items-start justify-between gap-4 pb-4 border-b last:border-b-0 last:pb-0">
                    <div className="flex-1 space-y-3">
                        <div className="flex-1 text-left">
                            <Input
                                className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2
                                text-base font-semibold leading-relaxed focus-visible:outline-none focus-visible:ring-2
                                focus-visible:ring-ring"
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
                                    className="flex w-20 rounded-md border border-input bg-background px-3 py-2
                                    text-base font-semibold text-foreground focus-visible:outline-none
                                    focus-visible:ring-2 focus-visible:ring-ring"
                                    type="number"
                                    value={exercise.sets}
                                    onChange={e => {
                                        setDraftExercises(prev =>
                                            prev.map((ex, i) =>
                                                (i === index ? {...ex, sets: Number(e.target.value)} : ex))
                                        )
                                    }}
                                />
                                <div className="text-xs">sets</div>
                            </div>
                            <div className="text-muted-foreground">×</div>
                            <div className="text-center">
                                <Input
                                    className="flex w-20 rounded-md border border-input bg-background px-3 py-2
                                    text-base font-semibold text-foreground focus-visible:outline-none
                                    focus-visible:ring-2 focus-visible:ring-ring"
                                    type="number"
                                    value={exercise.reps}
                                    onChange={e => {
                                        setDraftExercises(prev =>
                                            prev.map((ex, i) =>
                                                (i === index ? {...ex, reps: Number(e.target.value)} : ex))
                                        )
                                    }}
                                />
                                <div className="text-xs">reps</div>
                            </div>
                            <button
                                className="text-xs text-red-600 underline hover:text-red-700"
                                onClick={() => handleDelete(exercise.id)}
                            >
                                Delete exercise
                            </button>
                        </div>

                        <select
                            value={exercise.muscleGroup}
                            onChange={e => {
                                setDraftExercises(prev =>
                                    prev.map((ex, i) =>
                                        (i === index ? {...ex, muscleGroup: e.target.value as MuscleGroup} : ex))
                                )
                            }}
                            className="flex w-full rounded-md border border-input bg-background px-3 py-2 text-sm
                            focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring"
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

            <Label htmlFor="exercise-select" className="block text-sm font-medium">
                Add exercise from library:
            </Label>
            <Select onValueChange={(id) => addFromLibrary(id)}>
                <SelectTrigger className="flex w-full rounded-md border border-input bg-background px-3 py-2
                text-sm text-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring">
                    <SelectValue placeholder="Choose exercise..."/>
                </SelectTrigger>
                <SelectContent className="max-h-60">
                    {library.map(ex => (
                        <SelectItem key={ex.id} value={ex.id}>
                            {ex.name} · {ex.sets}×{ex.reps}
                        </SelectItem>
                    ))}
                </SelectContent>
            </Select>
        </div>
    )

}