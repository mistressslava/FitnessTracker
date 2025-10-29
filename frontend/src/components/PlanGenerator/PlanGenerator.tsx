import axios from "axios";
import {Button} from "@/components/ui/button.tsx";
import {useState} from "react";
import type {WorkoutPlan} from "@/types/WorkoutPlan.ts";

export default function PlanGenerator() {

    const [prompt, setPrompt] = useState("for beginner, 7 days");
    const [plan, setPlan] = useState<WorkoutPlan | null>(null);
    const [error, setError] = useState<string | null>(null);
    const [loading, setLoading] = useState(false);

    function generatePlan() {
        if (!prompt.trim()) {
            setError("Prompt is empty");
            return;
        }

        setLoading(true);

        axios.post<WorkoutPlan>("/api/generate-plan", {prompt: prompt}, {
            headers: {"Content-Type": "application/json"}
        })
            .then((res) => {
                setPlan(res.data)
                alert("✅ Your workout plan was successfully generate! ✅")
                setError(null);
            })
            .catch(e => {
                const data = e?.response?.data;
                const msg =
                    typeof data === "string"
                        ? data
                        : data?.message
                            ? data.message
                            : data?.error
                                ? data.error
                                : e?.message ?? "Request failed";

                setError(msg);
                setPlan(null);
            })
            .finally(() => setLoading(false));
    }

    return (
        <div className="space-y-3">
            <label className="block text-xl font-medium">Your prompt for AI generation</label>
            <textarea
                className="w-full border rounded p-2"
                rows={3}
                value={prompt}
                onChange={e => setPrompt(e.target.value)}
            />
            <Button
                className="w-90 rounded-xl border px-4 py-2 disabled:opacity-50"
                onClick={() => {
                    generatePlan();
                }}
                disabled={loading}
            >
                {loading ? "Generating..." : "Generate plan"}
            </Button>
            {error && <p className="text-red-600">{String(error)}</p>}
            {plan && <p>Plan was generated and saved to your plan library: {plan.title}</p>}
        </div>
    )
}