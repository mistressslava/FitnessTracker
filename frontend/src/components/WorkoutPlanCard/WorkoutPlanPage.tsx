import {useEffect, useState} from "react";
import axios from "axios";
import type {WorkoutPlan} from "@/types/WorkoutPlan.ts";
import {Card, CardContent, CardHeader, CardTitle} from "@/components/ui/card.tsx";
import {Badge} from "@/components/ui/badge";


export default function WorkoutPlanPage() {
    const [plans, setPlans] = useState<WorkoutPlan[]>([]);
    // const [current, setCurrent] = useState<WorkoutPlan | null>(null);
    const [loading, setLoading] = useState(true);
    // const [busyId, setBusyId] = useState<string | null>(null);
    const [error, setError] = useState<string>("");

    const [openPlanId, setOpenPlanId] = useState<string | null>(null);

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

    function togglePLan(id: string) {
        setOpenPlanId(prev => (prev === id ? null : id));
    }

    useEffect(() => {
        getAllWorkoutPlans();
    }, []);

    /*    function makeCurrent(id: string) {
            setBusyId(id);
            axios.put(`/api/workout-plans/${id}`)
                .then(res => {
                    setCurrent(res.data);
                    setError("");
                })
                .catch(e => setError(e.response.data ?? "Failed to set current plan"))
                .finally(() => setBusyId(null));
        }*/

    if (loading) {
        return (
            <div className="max-w-5xl mx-auto p-6 text-gray-300">
                Loading...
            </div>
        );
    }

    if (error) {
        return (
            <div className="text-red-6000">
                Failed: {error}
            </div>
        )
    }

    return (
        <div className="max-w-6x1 mx-auto space-y-6">
            <h1 className="text-5xl md:text-5xl font-bold text-foreground tracking-tight text-balance">
                All <span className="text-primary">PLANS</span>:
            </h1>
            <div className="flex flex-col gap-3 w-full max-w-6xl mx-auto">
                {plans.map((plan) => (

                    <div key={plan.id} className="border rounded-lg p-4 shadow-sm">
                        <button
                            onClick={() => togglePLan(plan.id)}
                            className="text-primary font-semibold text-xl hover:underline"
                        >
                            {plan.title}
                        </button>

                        {openPlanId === plan.id && (
                            <div className="mt-3 text-secondary-foreground">
                                <p className="italic">{plan.description}</p>
                                <p className="text-muted-foreground">
                                    {plan.days.reduce((total, day) =>
                                        total + day.exercises.length, 0)} total exercises across {" "}
                                    {plan.days.length} {" "}
                                    days
                                </p>
                                <div className="grid gap-4 md:grid-cols-3 lg-grid-cols-3">
                                    {plan.days.map((dayPLan) => (
                                        <Card key={dayPLan.id} className="overflow-hidden">
                                            <CardHeader className="bg-muted/50">
                                                <CardTitle className="flex items-center justify-between">
                                                    <div className="flex flex-col gap-1 text-left">
                                                        {dayPLan.day}
                                                        {dayPLan.type &&
                                                            <span className="text-sm font-normal text-muted-foreground">
                                                                {dayPLan.type}
                                                            </span>
                                                        }
                                                    </div>
                                                    <Badge variant="secondary">
                                                        {dayPLan.exercises.length} {dayPLan.exercises.length === 1 ?
                                                        "exercise" : "exercises"}
                                                    </Badge>
                                                </CardTitle>
                                            </CardHeader>
                                            <CardContent className="pt-6">
                                                <div className="space-y-4">
                                                    {dayPLan.type === "REST" ? <h3>Rest Day</h3> : ""}
                                                    {dayPLan.exercises.map((exercise) => (
                                                        <div
                                                            key={exercise.id}
                                                            className="flex items-start justify-between gap-4 pb-4 border-b
                                                            last:border-b-0 last:pb-0"
                                                        >
                                                            <div className="flex-1 text-left">
                                                                <h3 className="font-semibold text-sm leading-relaxed">
                                                                    {exercise.name}
                                                                </h3>
                                                            </div>
                                                            {exercise.sets > 0 && exercise.reps > 0 && (
                                                                <div
                                                                    className="flex gap-3 text-sm text-muted-foreground shrink-0">
                                                                    <div className="text-center">
                                                                        <div className="font-semibold text-foreground">
                                                                            {exercise.sets}
                                                                        </div>
                                                                        <div className="text-xs">sets</div>
                                                                    </div>
                                                                    <div className="text-muted-foreground">×</div>
                                                                    <div className="text-center">
                                                                        <div
                                                                            className="font-semibold text-foreground">{exercise.reps}</div>
                                                                        <div className="text-xs">reps</div>
                                                                    </div>
                                                                </div>
                                                            )}
                                                        </div>
                                                    ))}
                                                </div>

                                            </CardContent>
                                        </Card>
                                    ))}
                                </div>
                            </div>
                        )}
                    </div>
                ))}
            </div>

        </div>
    )
}