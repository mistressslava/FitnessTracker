import {useEffect, useState} from "react";
import axios from "axios";
import CreateWorkoutDay from "./CreateWorkoutDay";
import type {WorkoutDay} from "@/types/WorkoutDay.ts";
import type {WorkoutDayDto} from "@/types/WorkoutDayDto.ts";
import {Card} from "@/components/ui/card.tsx";
import WorkoutDayCard from "@/components/WorkoutDayCard/WorkoutDayCard.tsx";
import UpdateWorkoutDay from "@/components/WorkoutDayCard/UpdateWorkoutDay.tsx";

export default function WorkoutDaysPage() {
    const [days, setDays] = useState<WorkoutDay[]>([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string>("");
    const [isAdding, setIsAdding] = useState(false);

    const [editingDay, setEditingDay] = useState<WorkoutDay | null>(null);
    const [isEditing, setIsEditing] = useState(false);

    function getAllDays() {
        setLoading(true);
        axios.get<WorkoutDay[]>("/api/workout-days")
            .then(r => {
                setDays(r.data);
                setError("");
            })
            .catch(e => setError(String(e.response?.data ?? e.message)))
            .finally(() => setLoading(false));
    }

    useEffect(() => {
        getAllDays();
    }, []);

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
        <div className="mx-auto space-y-6">
            <h1 className="text-5xl md:text-5xl font-bold text-foreground tracking-tight text-balance">
                <span className="text-primary">Create</span> your custom <span className="text-primary">WORKOUT</span>:
            </h1>
            <Card className="bg-card border-border p-8 max-w-6xl mx-auto">
                <button
                    onClick={() => setIsAdding(prev => !prev)}
                    className={`w-80 px-4 py-2 rounded-xl mx-auto transition-colors
                    ${isAdding
                        ? "bg-destructive text-destructive-foreground hover:bg-destructive/80"
                        : "bg-primary text-primary-foreground hover:bg-primary/90"}
                    `}
                >
                    {isAdding ? "Cancel" : "Add New Day"}
                </button>

                {isAdding && <CreateWorkoutDay onAdd={createDay}/>}

                <div className="grid grid-cols-1 sm:grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                    {days.map(day => {
                        const editing = isEditing && editingDay?.id === day.id;

                        return (
                            <Card key={day.id} className="p-3 rounded-xl border">
                                <div className="flex items-center justify-between">
                                    <div className="flex flex-col gap-1 text-left">
                                        {day.day}
                                        {day.type &&
                                            <span className="text-sm font-normal text-muted-foreground">
                                            {day.type}
                                        </span>
                                        }
                                        <div className="text-m gap-2">
                                            {day.targetMuscles?.length ? <> {day.targetMuscles.join(", ")}</> : null}
                                        </div>
                                    </div>
                                    <div className="flex flex-col space-y-1.5">
                                        <button className="text-xs text-primary underline"
                                                onClick={() => {
                                                    setEditingDay(day);
                                                    setIsEditing(true);
                                                }}>
                                            Update
                                        </button>
                                        <button className="text-xs text-red-600 underline"
                                                onClick={() => deleteDay(day.id)}>
                                            Delete
                                        </button>
                                    </div>
                                </div>

                                {!editingDay && !editing && day.exercises?.length > 0 && (
                                    <WorkoutDayCard workoutDay={day}/>
                                )}

                                {editingDay && editing && (
                                    <UpdateWorkoutDay
                                        workoutDay={editingDay}
                                        onSaved={saved => {
                                            setDays(prev =>
                                                prev.map(d => (d.id === saved.id ? saved : d))
                                            );
                                            setEditingDay(null);
                                            setIsEditing(false)
                                        }}
                                        onCancel={() => {
                                            setEditingDay(null);
                                            setIsEditing(false);
                                        }}
                                    />
                                )}

                            </Card>
                        )
                    })}
                </div>
            </Card>
        </div>
    );
}
