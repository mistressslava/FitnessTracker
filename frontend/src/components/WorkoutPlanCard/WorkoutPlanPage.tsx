import {useEffect, useState} from "react";
import axios from "axios";
import type {WorkoutPlan} from "../../types/WorkoutPlan.ts";


export default function WorkoutPlanPage() {
    const [plans, setPlans] = useState<WorkoutPlan[]>([]);
    const [current, setCurrent] = useState<WorkoutPlan | null>(null);
    const [loading, setLoading] = useState(true);
    const [busyId, setBusyId] = useState<string | null>(null);
    const [error, setError] = useState<string>("");

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

    useEffect(() => {
        getAllWorkoutPlans();
    }, []);

    function makeCurrent(id:string) {
        setBusyId(id);
        axios.put(`/api/workout-plans/${id}`)
            .then(res => {
                setCurrent(res.data);
                setError("");
            })
            .catch(e => setError(e.response.data ?? "Failed to set current plan"))
            .finally(() => setBusyId(null));
    }

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

    return(
        <div>
            <aside className="rounded-2xl border border-zinc-700 p-4">
                <h3 className="mb-3 text-lg font-medium text-black">All plans</h3>
                <ul className="space-y-2">
                    {plans.map((p) => {
                        const isCurrent = current?.id === p.id;
                        return (
                            <li key={p.id} className="group">
                                <div className="flex items-center justify-between rounded-lg border border-zinc-700/60 bg-zinc-900 px-3 py-2 group-hover:bg-zinc-800">
                                    <span className="truncate">{p.title}</span>

                                    <button
                                        disabled={isCurrent || busyId === p.id}
                                        onClick={() => makeCurrent(p.id)}
                                        className={
                                            "ml-2 rounded px-2 py-1 text-sm " +
                                            (isCurrent
                                                ? "cursor-default bg-emerald-600/20 text-emerald-300"
                                                : "bg-emerald-500 text-black hover:bg-emerald-400 disabled:opacity-60")
                                        }
                                        title={isCurrent ? "Already current" : "Set as current"}
                                    >
                                        {isCurrent ? "Current" : busyId === p.id ? "..." : "Set"}
                                    </button>
                                </div>
                            </li>
                        );
                    })}
                </ul>
            </aside>

        </div>
    )
}