import type {WorkoutDay} from "../../types/WorkoutDay.ts";
import type {WorkoutDayDto} from "../../types/WorkoutDayDto.ts";
import {useState} from "react";
import {type DayOfWeek} from "../../types/DayOfWeek.ts";

type DaySlotProps = {
    weekDay: DayOfWeek;
    library: WorkoutDayDto[];
    value?: WorkoutDayDto;
    onChange: (dayDto: WorkoutDayDto) => void;
    onCreatedToLibrary: (created: WorkoutDay) => void;
}

export default function DaySlot(props: Readonly<DaySlotProps>) {

    const [mode, setMode] = useState<"idle" | "create">("idle");

    return (
        <div className="rounded-xl border p-3 space-y-2">
            <div className="flex items-center justify-between">
                <h4 className="font-semibold">{props.weekDay}</h4>
                <div className="flex gap-2">
                    <button
                        className="text-sm underline"
                        onClick={() => setMode(m => (m === "create" ? "idle" : "create"))}
                    >
                        {mode === "create" ? "← Choose existed" : "＋ Create new"}
                    </button>
                </div>
            </div>

            {mode === "idle" && (
                <div className="space-y-2">
                    <select
                        className="w-full border rounded px-2 py-1"
                        value={props.value?.id ?? ""}
                        onChange={e => {
                            const id = e.target.value;
                            const chosen = props.library.find(d => d.id === id);
                            if (chosen) {
                                props.onChange({...chosen, day: props.weekDay});
                            }
                        }}
                    >
                        <option value="">— Choose from existed —</option>
                        {props.library.map(d => (
                            <option key={d.id} value={d.id}>
                                {d.day} • {d.type} • {d.exercises?.[0]?.name ?? "No exercises"}
                            </option>
                        ))}
                    </select>

                    {props.value && props.value.id && (
                        <p className="text-xs text-gray-500">
                            Existing day selected: <b>{props.value.type}</b> ({props.value.exercises.length} exercises)
                        </p>
                    )}
                </div>
            )}

{/*            {mode === "create" && (
                <div className="border rounded-lg p-2 bg-gray-50">
                    <CreateWorkoutDay onAdd={onAddWorkoutDay}/>
                </div>
            )}*/}

        </div>
    )
}

