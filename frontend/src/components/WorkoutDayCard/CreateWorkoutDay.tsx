import {type FormEvent, useMemo, useState} from "react";
import type {WorkoutDayDto} from "../../types/WorkoutDayDto.ts";
import type {MuscleGroup} from "../../types/MuscleGroup.ts";
import type {WorkoutDayType} from "../../types/WorkoutDayType.ts";
import type {DayOfWeek} from "../../types/DayOfWeek.ts";
import type {Exercise} from "../../types/Exercise.ts";
import {useExercises} from "../ExerciseCard/UseExercises.ts";

type CreateWorkoutDayProps = {
    onAdd: (dto: WorkoutDayDto) => void;
}

const DAYS: DayOfWeek[] = ["MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY","SUNDAY"];
const TYPES: WorkoutDayType[] = ["UPPER_BODY", "LOWER_BODY", "FULL_BODY", "REST"];
// const MUSCLES: MuscleGroup[] = ["CHEST","BACK","LEGS","SHOULDERS","ARMS","CORE","GLUTES"];

export default function CreateWorkoutDay({onAdd}: Readonly<CreateWorkoutDayProps>) {
    const [workoutDto, setWorkoutDto] = useState<WorkoutDayDto>({
        day: "MONDAY",
        type: "UPPER_BODY",
        targetMuscles: [],
        exercises: [{name: "", sets: 3, reps: 8}],
    });

    const [error, setError] = useState<string>("");

    const {exercises: library, loading: libLoading, error: libError } = useExercises();

    const isRest = workoutDto.type === "REST";

    function setField<K extends keyof WorkoutDayDto>(key: K, value: WorkoutDayDto[K]) {
        setWorkoutDto(prev => ({ ...prev, [key]: value }));
    }

    function updateExercise(i: number, patch: Partial<WorkoutDayDto["exercises"][number]>) {
        setWorkoutDto(prev => {
            const exercises = prev.exercises.map((ex, idx) => (idx === i ? { ...ex, ...patch } : ex));
            return { ...prev, exercises };
        });
    }

    function addEmptyExercise() {
        if (isRest) return;
        setWorkoutDto(prev => ({ ...prev, exercises: [...prev.exercises, { name: "", sets: 3, reps: 8 }] }));
    }

    function removeExerciseRow(i: number) {
        if (isRest) return;
        setWorkoutDto(prev => ({ ...prev, exercises: prev.exercises.filter((_, idx) => idx !== i) }));
    }

    function addFromLibrary(exId: string) {
        if (isRest) return;
        const lib = library.find((x: Exercise) => x.id === exId);
        if (!lib) return;
        setWorkoutDto(prev => ({
            ...prev,
            exercises: [
                ...prev.exercises,
                { exerciseId: lib.id, name: lib.name, sets: lib.sets, reps: lib.reps },
            ],
        }));
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
            ? { ...workoutDto, targetMuscles: [], exercises: [] }
            : { ...workoutDto };

        const msg = validate(normalized);
        if (msg) { setError(msg); return; }

        onAdd(normalized);
        // reset
        setWorkoutDto({
            day: "MONDAY",
            type: "UPPER_BODY",
            targetMuscles: [],
            exercises: [{ name: "", sets: 3, reps: 8 }],
        });
    }

    const libraryPlaceholder = useMemo(() => {
        if (libLoading) return "Loading…";
        if (libError) return "Failed to load";
        if (library.length === 0) return "No exercises";
        return "Choose…";
    }, [libLoading, libError, library.length]);



    return (
        <form onSubmit={handleSubmit} className="space-y-4 p-4 rounded-2xl border">
            <div className="grid grid-cols-1 md:grid-cols-3 gap-3">
                <label className="flex flex-col gap-1">
                    <span className="text-sm font-medium">Day</span>
                    <select
                        className="rounded-lg border px-3 py-2"
                        value={workoutDto.day}
                        onChange={e => setField("day", e.target.value as DayOfWeek)}
                    >
                        {DAYS.map(d => <option key={d} value={d}>{d}</option>)}
                    </select>
                </label>

                <label className="flex flex-col gap-1">
                    <span className="text-sm font-medium">Type</span>
                    <select
                        className="rounded-lg border px-3 py-2"
                        value={workoutDto.type}
                        onChange={e => setField("type", e.target.value as WorkoutDayType)}
                    >
                        {TYPES.map(t => <option key={t} value={t}>{t}</option>)}
                    </select>
                </label>

                <label className="flex flex-col gap-1">
                    <span className="text-sm font-medium">Target muscles (optional)</span>
                    <input
                        className="rounded-lg border px-3 py-2"
                        placeholder="CHEST, BACK … or nothing"
                        value={workoutDto.targetMuscles.join(", ")}
                        onChange={e =>
                            setField(
                                "targetMuscles",
                                e.target.value.split(",").map(s => s.trim()) as MuscleGroup[]
                            )
                        }
                        disabled={isRest}
                    />
                </label>
            </div>

            {!isRest && (
                <div className="space-y-3">
                    <div className="flex items-center justify-between">
                        <h3 className="font-semibold">Exercises</h3>
                        <div className="flex gap-3">
                            <button type="button" onClick={addEmptyExercise} className="text-sm underline">
                                + Add empty
                            </button>

                            <label className="text-sm flex items-center gap-2">
                                <span>Add from library:</span>
                                <select
                                    className="rounded-lg border px-2 py-1"
                                    defaultValue=""
                                    onChange={e => { if (e.target.value) { addFromLibrary(e.target.value); e.currentTarget.value = ""; } }}
                                >
                                    <option value="" disabled>{libraryPlaceholder}</option>
                                    {library.map(ex => (
                                        <option key={ex.id} value={ex.id}>
                                            {ex.name} · {ex.sets}×{ex.reps}
                                        </option>
                                    ))}
                                </select>
                            </label>
                        </div>
                    </div>

                    {workoutDto.exercises.map((ex, i) => (
                        <div key={i} className="grid grid-cols-1 md:grid-cols-12 gap-2 items-end">
                            <label className="md:col-span-6 flex flex-col gap-1">
                                <span className="text-xs">Name</span>
                                <input
                                    className="rounded-lg border px-3 py-2"
                                    value={ex.name}
                                    onChange={e => updateExercise(i, { name: e.target.value })}
                                    placeholder="Bench Press"
                                />
                            </label>
                            <label className="md:col-span-3 flex flex-col gap-1">
                                <span className="text-xs">Sets</span>
                                <input
                                    type="number" min={1}
                                    className="rounded-lg border px-3 py-2"
                                    value={ex.sets}
                                    onChange={e => updateExercise(i, { sets: Number(e.target.value) })}
                                />
                            </label>
                            <label className="md:col-span-3 flex flex-col gap-1">
                                <span className="text-xs">Reps</span>
                                <input
                                    type="number" min={1}
                                    className="rounded-lg border px-3 py-2"
                                    value={ex.reps}
                                    onChange={e => updateExercise(i, { reps: Number(e.target.value) })}
                                />
                            </label>
                            <div className="md:col-span-12">
                                <button type="button" onClick={() => removeExerciseRow(i)} className="text-xs text-red-600 underline">
                                    Remove
                                </button>
                            </div>
                        </div>
                    ))}
                </div>
            )}

            {error && <p className="text-red-600 text-sm">{error}</p>}
            <button type="submit" className="px-4 py-2 rounded-xl border">Create day</button>
        </form>
    );
}