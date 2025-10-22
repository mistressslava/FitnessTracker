import { useEffect, useState } from "react";
import axios from "axios";
import CreateWorkoutDay from "./CreateWorkoutDay";
import type { WorkoutDay } from "../../types/WorkoutDay";
import type {WorkoutDayDto} from "../../types/WorkoutDayDto.ts";

export default function WorkoutDaysPage() {
    const [days, setDays] = useState<WorkoutDay[]>([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string>("");
    const [isAdding, setIsAdding] = useState(false);

    function getAllDays() {
        setLoading(true);
        axios.get<WorkoutDay[]>("/api/workout-days")
            .then(r => { setDays(r.data); setError(""); })
            .catch(e => setError(String(e.response?.data ?? e.message)))
            .finally(() => setLoading(false));
    }

    useEffect(() => { getAllDays(); }, []);

    //CREATE
    function createDay(dto: WorkoutDayDto) {
        return axios.post<WorkoutDay>("/api/workout-days", dto)
            .then(r => {
                setDays(prev => [r.data, ...prev]);
                setIsAdding(false);
            })
            .catch(e => setError(String(e.response?.data ?? e.message)));
    }

    //DELETE
    function deleteDay(id: string) {
        return axios.delete(`/api/workout-days/${id}`)
            .then(() => setDays(prev => prev.filter(d => d.id !== id)))
            .catch(e => setError(String(e.response?.data ?? e.message)));
    }

    if (loading) return <p>Loading…</p>;
    if (error) return <p className="text-red-600">Failed: {error}</p>;

    return (
        <div>
            <h2>Your workout days</h2>

            <button
                onClick={() => setIsAdding(p => !p)}
                className="bg-indigo-600 text-black px-4 py-2 rounded-xl hover:bg-indigo-700 mb-4"
            >
                {isAdding ? "Cancel" : "Add New Day"}
            </button>

            {isAdding && <CreateWorkoutDay onAdd={createDay} />}

            <ul className="space-y-3">
                {days.map(d => (
                    <li key={d.id} className="p-3 rounded-xl border">
                        <div className="flex items-center justify-between">
                            <div>
                                <b>{d.day}</b> — {d.type}
                                {d.targetMuscles?.length ? <> · {d.targetMuscles.join(", ")}</> : null}
                            </div>
                            <button className="text-xs text-red-600 underline" onClick={() => deleteDay(d.id)}>
                                Delete
                            </button>
                        </div>
                        {d.exercises?.length > 0 && (
                            <div className="text-sm mt-1">
                                {d.exercises.map((ex) => (
                                    <div key={ex.id}>{ex.name}: {ex.sets}×{ex.reps}</div>
                                ))}
                            </div>
                        )}
                    </li>
                ))}
            </ul>
        </div>
    );
}
