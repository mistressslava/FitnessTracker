import {type FormEvent, useMemo, useState} from "react";
import type {WorkoutDayDto} from "@/types/WorkoutDayDto.ts";
import {MUSCLE_GROUPS, type MuscleGroup} from "@/types/MuscleGroup.ts";
import {WORKOUT_DAY_TYPES, type WorkoutDayType} from "@/types/WorkoutDayType.ts";
import {DAY_OF_WEEK_VALUES, type DayOfWeek} from "@/types/DayOfWeek.ts";
import type {Exercise} from "@/types/Exercise.ts";
import {useExercises} from "../ExerciseCard/UseExercises.ts";
import {Label} from "@/components/ui/label.tsx";
import {Input} from "@/components/ui/input.tsx";
import {Button} from "@/components/ui/button.tsx";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select.tsx";

type CreateWorkoutDayProps = {
    onAdd: (dto: WorkoutDayDto) => void;
}

export default function CreateWorkoutDay({onAdd}: Readonly<CreateWorkoutDayProps>) {
    const [workoutDto, setWorkoutDto] = useState<WorkoutDayDto>({
        day: "MONDAY",
        type: "UPPER_BODY",
        targetMuscles: [],
        exercises: [],
    });

    const [error, setError] = useState<string>("");

    const {exercises: library, loading: libLoading, error: libError} = useExercises();

    const isRest = workoutDto.type === "REST";

    function setField<K extends keyof WorkoutDayDto>(key: K, value: WorkoutDayDto[K]) {
        setWorkoutDto(prev => ({...prev, [key]: value}));
    }

    function updateExercise(i: number, patch: Partial<WorkoutDayDto["exercises"][number]>) {
        setWorkoutDto(prev => {
            const exercises = prev.exercises.map((ex, idx) => (idx === i ? {...ex, ...patch} : ex));
            return {...prev, exercises};
        });
    }

    function addEmptyExercise() {
        if (isRest) return;
        setWorkoutDto(prev => ({
            ...prev,
            exercises: [...prev.exercises, {name: "", sets: 3, reps: 8, muscleGroup: "CHEST" as MuscleGroup}]
        }));
    }

    function removeExerciseRow(i: number) {
        if (isRest) return;
        setWorkoutDto(prev => ({...prev, exercises: prev.exercises.filter((_, idx) => idx !== i)}));
    }

    function addFromLibrary(exId: string) {
        if (isRest) return;
        const lib = library.find((x: Exercise) => x.id === exId);
        if (!lib) return;
        setWorkoutDto(prev => {
            const nextExercises = [
                ...prev.exercises,
                {
                    name: lib.name,
                    sets: lib.sets,
                    reps: lib.reps,
                    muscleGroup: lib.muscleGroup,
                },
            ];

            const nextTargets = lib.muscleGroup && !prev.targetMuscles.includes(lib.muscleGroup)
                ? [...prev.targetMuscles, lib.muscleGroup]
                : prev.targetMuscles;

            return {...prev, exercises: nextExercises, targetMuscles: nextTargets};
        });
    }


    function validate(dto: WorkoutDayDto): string | null {
        if (!dto.day) return "Choose a day of the week";
        if (!dto.type) return "Choose a type of workout";
        if (dto.type === "REST") {
            if (dto.targetMuscles.length > 0) return "For REST you must not add target Muscles.";
            if (dto.exercises.length > 0) return "For REST exercises must be empty.";
            return null;
        }
        if (dto.exercises.length === 0) return "Add at least one exercise.";
        for (const [i, ex] of dto.exercises.entries()) {
            if (!ex.name.trim()) return `Exercise #${i + 1}: name is empty.`;
            if (ex.sets <= 0) return `Вправа #${i + 1}: sets must be > 0.`;
            if (ex.reps <= 0) return `Вправа #${i + 1}: reps must be > 0.`;
        }
        return null;
    }

    function handleSubmit(e: FormEvent) {
        e.preventDefault();
        setError("");

        const normalized: WorkoutDayDto = isRest
            ? {...workoutDto, targetMuscles: [], exercises: []}
            : {...workoutDto};

        const msg = validate(normalized);
        if (msg) {
            setError(msg);
            return;
        }

        onAdd(normalized);
        // reset
        setWorkoutDto({
            day: "MONDAY",
            type: "UPPER_BODY",
            targetMuscles: [],
            exercises: [{name: "", sets: 3, reps: 8, muscleGroup: undefined}],
        });
    }

    const libraryPlaceholder = useMemo(() => {
        if (libLoading) return <p>Loading…</p>;
        if (libError) return <p>Failed to load</p>;
        if (library.length === 0) return "No exercises";
        return "Choose…";
    }, [libLoading, libError, library.length]);


    return (
        <form onSubmit={handleSubmit}
              className="flex flex-col justify-center mx-auto gap-3 p-4 border rounded-2xl shadow h-auto w-auto">
            <div className="grid grid-cols-1 md:grid-cols-3 gap-3">
                <Label className="flex flex-col gap-1">
                    <span className="text-sm font-medium">Day</span>
                    <select
                        className="rounded-lg border px-3 py-2"
                        value={workoutDto.day}
                        onChange={e => setField("day", e.target.value as DayOfWeek)}
                    >
                        {DAY_OF_WEEK_VALUES.map(day => <option key={day} value={day}>{day}</option>)}
                    </select>
                </Label>

                <Label className="flex flex-col gap-1">
                    <span className="text-sm font-medium">Type</span>
                    <select
                        className="rounded-lg border px-3 py-2"
                        value={workoutDto.type}
                        onChange={e => setField("type", e.target.value as WorkoutDayType)}
                    >
                        {WORKOUT_DAY_TYPES.map(type => <option key={type} value={type}>{type}</option>)}
                    </select>
                </Label>
            </div>

            {!isRest && (
                <div className="space-y-6">
                    <h3 className="font-semibold mb-3">Exercises</h3>
                    <div className="space-y-1.5">
                        <Label htmlFor="exercise-select" className="text-sm">
                            Add from library:
                        </Label>
                        <Select onValueChange={(id) => addFromLibrary(id)}>
                            <SelectTrigger className="w-full">
                                <SelectValue placeholder={libraryPlaceholder}/>
                            </SelectTrigger>
                            <SelectContent className="max-h-60">
                                {library.map(ex => (
                                    <SelectItem key={ex.id} value={ex.id}>
                                        {ex.name} · {ex.sets}×{ex.reps}
                                    </SelectItem>
                                ))}
                            </SelectContent>
                        </Select>
                        <div className="space-y-1.5">
                            <Label htmlFor="target-muscles" className="text-sm font-medium">
                                Target muscles
                            </Label>
                                <select
                                    id="target-muscles"
                                    multiple
                                    size={8}
                                    className="flex justify-start rounded-lg border px-3 py-2 h-48"
                                    value={workoutDto.targetMuscles}
                                >
                                    {MUSCLE_GROUPS.map(group => (
                                        <option
                                            key={group}
                                            value={group}
                                            onMouseDown={(e) => {
                                                e.preventDefault();
                                                const opt = e.currentTarget as HTMLOptionElement;
                                                const select = opt.parentElement as HTMLSelectElement;

                                                opt.selected = !opt.selected;

                                                const values = Array.from(select.selectedOptions, o => o.value as MuscleGroup);
                                                setField("targetMuscles", values);
                                            }}
                                        >
                                            {group}
                                        </option>
                                    ))}
                                </select>
                        </div>
                    </div>
                    <Button
                        variant="secondary"
                        type="button"
                        onClick={addEmptyExercise} className="text-sm mr-auto ">
                        + Add empty
                    </Button>
                    {workoutDto.exercises.map((ex, i) => (
                        <div key={i} className="flex flex-1 md:grid-cols-12 justify-between items-end">

                            <Label className="md:col-span-6 flex flex-col gap-1">
                                <span className="text-xs">Name</span>
                                <Input
                                    type="text"
                                    value={ex.name}
                                    onChange={e => updateExercise(i, {name: e.target.value})}
                                    placeholder="Bench Press..."
                                    className='w-40 rounded-lg border px-3 py-2 h-12 border-border ${error.includes("Name") ? "border-red-600 border" : ""}'
                                    required
                                />
                            </Label>
                            <Label className="md:col-span-3 flex flex-col gap-1">
                                <span className="text-xs">Sets</span>
                                <Input
                                    type="number" min={1}
                                    value={ex.sets}
                                    onChange={e => updateExercise(i, {sets: Number(e.target.value)})}
                                    className='w-40 rounded-lg border px-3 py-2 h-12 border-border ${error.includes("Sets") ? "border-red-600 border" : ""}'
                                    required
                                />
                            </Label>
                            <Label className="md:col-span-3 flex flex-col gap-1">
                                <span className="text-xs">Reps</span>
                                <Input
                                    type="number" min={1}
                                    value={ex.reps}
                                    onChange={e => updateExercise(i, {reps: Number(e.target.value)})}
                                    className='rounded-lg border px-3 py-2 h-12 border-border ${error.includes("Reps") ? "border-red-600 border" : ""}'
                                    required
                                />
                            </Label>

                            <div className="md:col-span-12">
                                <button type="button" onClick={() => removeExerciseRow(i)}
                                        className="text-xs text-red-600 underline">
                                    Remove
                                </button>
                            </div>
                        </div>
                    ))}
                </div>
            )}

            {error && <p className="text-red-600 text-sm">{error}</p>}
            <button type="submit"
                    className="w-80 bg-primary text-primary-foreground px-4 py-2 rounded-xl hover:bg-primary/90 mx-auto">
                Create day
            </button>
        </form>
    );
}