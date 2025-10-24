import type {WorkoutDay} from "../../types/WorkoutDay.ts";
import type {WorkoutDayDto} from "../../types/WorkoutDayDto.ts";
import {useState} from "react";
import {type DayOfWeek} from "../../types/DayOfWeek.ts";
import CreateWorkoutDay from "../WorkoutDayCard/CreateWorkoutDay.tsx";

type DaySlotProps = {
    weekDay: DayOfWeek;
    library: WorkoutDayDto[];
    value?: WorkoutDayDto;
    onChange: (dayDto: WorkoutDayDto) => void;
    onCreatedToLibrary: (created: WorkoutDay) => void;
}

export default function DaySlot(props: Readonly<DaySlotProps>) {

    const [mode, setMode] = useState<"idle" | "create">("idle");

    const handleCreateLocal = (createdDto: WorkoutDayDto) => {
        props.onChange({ ...createdDto, day: props.weekDay });
        setMode("idle");
    };

    return (
        <>
            {mode === "idle" && (
                <div className="space-y-2">
                    <select
                        className="w-full border rounded px-2 py-1"
                        value={props.value?.id ?? ""}  // value stays empty for ad-hoc (non-library) day
                        onChange={e => {
                            const chosen = props.library.find(d => d.id === e.target.value);
                            if (chosen) props.onChange({ ...chosen, day: props.weekDay });
                        }}
                    >
                        <option value="">— Choose existing —</option>
                        {props.library.map(d => (
                            <option key={d.id} value={d.id}>
                                {d.type} • {(d.exercises ?? []).map(ex => ex.name).join(", ") || "No exercises"}
                            </option>
                        ))}
                    </select>

                    {/* Always show the CURRENT value summary, even if it’s not from library */}
                    {props.value && (
                        <p className="text-xs text-gray-500">
                            Selected for {props.weekDay}: <b>{props.value.type}</b> ({props.value.exercises.length} exercises)
                        </p>
                    )}
                </div>
            )}

            {mode === "create" && (
                <div className="border rounded-lg p-2 bg-gray-50">
                    <CreateWorkoutDay onAdd={handleCreateLocal} />
                </div>
            )}

            <button
                className="text-sm underline mt-2"
                onClick={() => setMode(m => (m === "create" ? "idle" : "create"))}
            >
                {mode === "create" ? "← Choose existing" : "＋ Create new"}
            </button>
        </>
    );
}

