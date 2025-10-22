import type {ExerciseDto} from "../../types/ExerciseDto.ts";
import {type FormEvent, useState} from "react";
import axios from "axios";
import type {Exercise} from "../../types/Exercise.ts";
import {MUSCLE_GROUPS, type MuscleGroup} from "../../types/MuscleGroup.ts";

type NewExerciseProps = {
    onAdd: (newExercise: Exercise) => void
}

export default function CreateExercise({onAdd}: Readonly<NewExerciseProps>) {
    const [exerciseDto, setExerciseDto] = useState<ExerciseDto>({name: "", sets: 0, reps: 0, muscleGroup: undefined});
    const [error, setError] = useState("");

    function onAddNewExercise(e: FormEvent) {
        e.preventDefault();

        axios.post('/api/exercises', exerciseDto)
            .then((res) => {
                onAdd(res.data);
                setExerciseDto({name: "", sets: 0, reps: 0, muscleGroup: undefined});
                setError("");
            })
            .catch(err => {
                if (err.response && typeof err.response.data === "string") {
                    setError(err.response.data)
                } else {
                    setError("Unexpected error occurred");
                }
                console.log(err)
            });
    }

    return (
        <form onSubmit={onAddNewExercise} className="flex flex-col gap-3 p-4 border rounded-2xl shadow">
            <label>Name:<input
                value={exerciseDto.name}
                onChange={e => setExerciseDto({...exerciseDto, name: e.target.value})}
                placeholder="Enter an exercise name..."
                className={'border ${error.includes("Reps") ? "border-red-600 border" : ""}'}
            />
            </label>

            <label>Sets:<input
                type="number"
                value={exerciseDto.sets}
                onChange={e => setExerciseDto({...exerciseDto, sets: Number(e.target.value)})}
                className={'border ${error.includes("Reps") ? "border-red-600 border" : ""}'}
                required
            />
            </label>

            <label>Reps:<input
                type="number"
                value={exerciseDto.reps}
                onChange={e => setExerciseDto({...exerciseDto, reps: Number(e.target.value)})}
                className={'border ${error.includes("Sets") ? "border-red-600 border" : ""}'}
                required
            />
            </label>

            <label>Muscle group:<select
                value={exerciseDto.muscleGroup}
                onChange={e => setExerciseDto({...exerciseDto, muscleGroup: e.target.value as MuscleGroup})}
                className={'border ${error.includes("Sets") ? "border-red-600 border" : ""}'}
            >
                <option value="">Select a muscle group</option>
                {Object.values(MUSCLE_GROUPS).map((group) => (
                    <option key={group} value={group}>
                        {group}
                    </option>
                ))}
            </select>
            </label>
            {error && <span className="text-red-600 text-sm">{error}</span>}

            <button type="submit">Add Exercise</button>
        </form>
    )

}