import {useEffect, useState} from "react";
import axios from "axios";
import type {WorkoutPlan} from "@/types/WorkoutPlan.ts";
import {Card, CardHeader, CardTitle} from "@/components/ui/card.tsx";
import {Badge} from "@/components/ui/badge";
import WorkoutDayCard from "@/components/WorkoutDayCard/WorkoutDayCard.tsx";
import UpdateWorkoutDay from "@/components/WorkoutDayCard/UpdateWorkoutDay.tsx";
import type {WorkoutDay} from "@/types/WorkoutDay.ts";


export default function WorkoutPlanPage() {
    const [plans, setPlans] = useState<WorkoutPlan[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string>("");

    const [openPlanId, setOpenPlanId] = useState<string | null>(null);

    const [editing, setEditing] = useState<{ planId: string; dayId: string } | null>(null);

    function getAllWorkoutPlans() {
        setLoading(true);
        axios.get("/api/workout-plans")
            .then(r => {
                setPlans(r.data);
                setError("")
            })
            .catch(e => console.log(e))
            .finally(() => setLoading(false));
    }

    //DELETE
    function deleteWorkoutPlan(id: string) {
        return axios.delete(`/api/workout-plans/${id}`)
            .then(() => setPlans(prev => prev.filter(plan => plan.id !== id)))
            .catch(e => setError(String(e.response?.data ?? e.message)));
    }

    useEffect(() => {
        getAllWorkoutPlans();
    }, []);

    function togglePLan(id: string) {
        setOpenPlanId(prev => (prev === id ? null : id));
        setEditing((prev) =>
            (prev && prev.planId === id ? null : prev));
    }

    const replaceDayInPlans = (planId: string, updatedDay: WorkoutDay) => {
        setPlans(prev =>
            prev.map(p =>
                p.id !== planId ? p : { ...p, days: p.days.map(d => d.id === updatedDay.id ? updatedDay : d) }
            )
        );
    };


    useEffect(() => {
        getAllWorkoutPlans();
    }, []);

    if (loading) return <p>Loading…</p>;
    if (error) return <p className="text-red-600">Failed: {error}</p>;


    return (
        <div className="max-w-6x1 mx-auto space-y-6">
            <h1 className="text-5xl md:text-5xl font-bold text-foreground tracking-tight text-balance">
                All <span className="text-primary">PLANS</span>:
            </h1>
            <div className="flex flex-col gap-3 w-full max-w-6xl mx-auto">
                {plans.map((plan) => (
                    <div key={plan.id} className="border rounded-lg p-4 shadow-sm">
                        <div className="flex items-center justify-between gap-4">
                            <button
                                onClick={() => togglePLan(plan.id)}
                                className="flex-1 text-center text-primary font-semibold text-xl hover:underline"
                            >
                                {plan.title}
                            </button>

                            <button className="text-xs text-red-600 underline"
                                    onClick={() => deleteWorkoutPlan(plan.id)}>
                                Delete
                            </button>
                        </div>

                        {openPlanId === plan.id && (
                            <div className="mt-3 text-secondary-foreground">
                                <p className="italic">{plan.description}</p>
                                <p className="text-muted-foreground">
                                    {plan.days.reduce((total, day) =>
                                        total + day.exercises.length, 0)} total exercises across {" "}
                                    {plan.days.length} {" "} days
                                </p>

                                <div className="grid gap-4 md:grid-cols-3 lg-grid-cols-3">
                                    {plan.days.map((day) => {
                                        const isEditing = editing?.planId === plan.id &&
                                            editing?.dayId === day.id;

                                        return (
                                            <Card key={day.id} className="overflow-hidden">
                                                <CardHeader className="bg-muted/50">
                                                    <CardTitle className="flex items-center pt-3 justify-between">
                                                        <div className="flex flex-col gap-1 text-left">
                                                            {day.day}
                                                            {day.type &&
                                                                <span
                                                                    className="text-sm font-normal text-muted-foreground">
                                                                {day.type}
                                                            </span>
                                                            }
                                                        </div>
                                                        <div className="flex flex-col gap-1 text-right">
                                                            <Badge variant="secondary">
                                                                {day.exercises.length} {day.exercises.length === 1 ?
                                                                "exercise" : "exercises"}
                                                            </Badge>
                                                            <button
                                                                className="text-xs text-primary underline"
                                                                onClick={() => setEditing({
                                                                    planId: plan.id,
                                                                    dayId: day.id
                                                                })}
                                                            >
                                                                Update
                                                            </button>
                                                        </div>
                                                    </CardTitle>
                                                </CardHeader>

                                                {!isEditing && (
                                                    <>
                                                        {day.exercises?.length > 0 && (
                                                            <WorkoutDayCard
                                                                isRestDay={day.type === "REST"}
                                                                workoutDay={day}
                                                            />
                                                        )}
                                                    </>
                                                )}

                                                {isEditing && (
                                                    <UpdateWorkoutDay
                                                        planId={plan.id}
                                                        plan={plan}
                                                        workoutDay={day}
                                                        onSaved={(updatedDay) => {
                                                            replaceDayInPlans(plan.id, updatedDay);
                                                            setEditing(null);
                                                        }}
                                                        onCancel={() => {
                                                            setEditing(null);
                                                        }}
                                                    />
                                                )}
                                            </Card>
                                        )
                                    })}
                                </div>
                            </div>
                        )}
                    </div>
                ))}
            </div>
        </div>
    )
}