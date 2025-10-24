import {useMemo, useState} from "react";
import axios from "axios";
import {useWorkoutDays} from "../WorkoutDayCard/useWorkoutDays.ts";
import {DAY_OF_WEEK_VALUES, type DayOfWeek} from "@/types/DayOfWeek.ts";
import type {WorkoutDay} from "@/types/WorkoutDay.ts";
import type {WorkoutDayDto} from "@/types/WorkoutDayDto.ts";
import DayCard from "./DayCard.tsx";
import {Card} from "@/components/ui/card.tsx";
import {Input} from "@/components/ui/input.tsx";
import { Textarea } from "@/components/ui/textarea"
import {Button} from "@/components/ui/button.tsx";

export default function CreateWorkoutPlan() {

    const {days: library, loading, error, addToLibrary} = useWorkoutDays();

    const [title, setTitle] = useState("");
    const [description, setDescription] = useState("");
    const [submitError, setSubmitError] = useState<string>("")

    const [selected, setSelected] = useState<Record<DayOfWeek, WorkoutDayDto | undefined>>({
        MONDAY: undefined, TUESDAY: undefined, WEDNESDAY: undefined,
        THURSDAY: undefined, FRIDAY: undefined, SATURDAY: undefined, SUNDAY: undefined
    });

    function updateDay(weekDay: DayOfWeek, dto: WorkoutDayDto) {
        setSelected(prev => ({...prev, [weekDay]: dto}));
    }

    const isComplete = useMemo(() => DAY_OF_WEEK_VALUES.every(d => !!selected[d]), [selected]);

    const planDto: { title: string; description: string; days: WorkoutDayDto[] } | null = isComplete
        ? {
            title: title.trim(),
            description: description.trim(),
            days: DAY_OF_WEEK_VALUES.map(day => selected[day]!)
        }
        : null;

    function createPlan() {
        if (!planDto) {
            setSubmitError("Please, fill all 7 days.");
            return;
        }
        if (!planDto.description) {
            setSubmitError("Please add the title.");
            return;
        }

        axios.post("/api/workout-plans", planDto)
            .then(() => {
                alert("✅ Your workout plan was successfully saved! ✅");
            })
            .catch(e => {
                console.error(e);
                setSubmitError(e?.response?.data ?? "The plan could not to be saved")
            })
    }

    if (loading) return <p>Loading workout library...</p>;
    if (error) return <p>❌Failed to load days.❌</p>

    return (
        <>
            <h1 className="text-5xl md:text-5xl font-bold text-foreground tracking-tight text-balance">
                Create your custom <span className="text-primary">PLAN</span>:
            </h1>
            <Card>
                <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                    <div className="lg:col-span-1 space-y-2">
                        <Input
                            className="w-90 h-15 border border-border rounded px-3 py-2"
                            placeholder="Plan name..."
                            value={title}
                            onChange={e => setTitle(e.target.value)}
                        />
                        <Textarea
                            className="w-90 h-35 ml-3 border border-border rounded px-3 py-3"
                            placeholder="Description..."
                            rows={4}
                            value={description}
                            onChange={e => setDescription(e.target.value)}
                        />
                        <Button
                            className="w-90 rounded-xl border px-4 py-2 disabled:opacity-50"
                            onClick={createPlan}
                            disabled={!isComplete || !title.trim()}
                        >
                            Save plan
                        </Button>
                        {submitError && <p className="text-red-600 text-sm">{submitError}</p>}
                    </div>

                    <div className="lg:col-span-2 grid sm:grid-cols-2 gap-4">
                        {DAY_OF_WEEK_VALUES.map(d => (
                            <DayCard
                                key={d}
                                weekDay={d}
                                library={library}
                                value={selected[d]}
                                onChange={(dto) => updateDay(d, dto)}
                                onCreatedToLibrary={(created: WorkoutDay) => {
                                    addToLibrary(created);
                                    updateDay(d, created);
                                }}
                            />
                        ))}
                    </div>
                </div>
            </Card>
        </>
    )
}