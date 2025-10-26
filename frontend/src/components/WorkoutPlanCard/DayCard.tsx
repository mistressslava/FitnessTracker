import type {WorkoutDay} from "@/types/WorkoutDay.ts";
import type {WorkoutDayDto} from "@/types/WorkoutDayDto.ts";
import {useState} from "react";
import {type DayOfWeek} from "@/types/DayOfWeek.ts";
import CreateWorkoutDay from "../WorkoutDayCard/CreateWorkoutDay.tsx";
import {Select, SelectContent, SelectItem, SelectTrigger} from "@/components/ui/select.tsx";
import {Label} from "@/components/ui/label.tsx";
import {CirclePlus} from 'lucide-react';

type DaySlotProps = {
    weekDay: DayOfWeek;
    library: WorkoutDay[];
    value?: WorkoutDayDto;
    onChange: (dayDto: WorkoutDayDto) => void;
    onCreatedToLibrary: (created: WorkoutDay) => void;
}

export default function DayCard(props: Readonly<DaySlotProps>) {

    const [mode, setMode] = useState<"idle" | "create">("idle");

    const handleCreateLocal = (createdDto: WorkoutDayDto) => {
        props.onChange({...createdDto, day: props.weekDay});
        setMode("idle");
    };

    return (
        <>
            {mode === "idle" && (
                <div className="space-y-2">
                    <Label className="mr-100">{props.weekDay}</Label>
                    <Select
                        value={props.value?.id ?? ""}  // value stays empty for ad-hoc (non-library) day
                        onValueChange={e => {
                            const chosen = props.library.find(d => d.id === e);
                            if (chosen) props.onChange({...chosen, day: props.weekDay});
                        }}
                        required
                    >
                        <SelectTrigger className="select-trigger w-120 border rounded px-2 py-1">— Choose existing
                            —</SelectTrigger>
                        <SelectContent position="popper">
                            {props.library.map((d) => (
                                <SelectItem key={d.id} value={String(d.id)}>
                                    {d.type} • {(d.exercises ?? []).map(ex => ex.name).join(", ") || "No exercises"}
                                </SelectItem>
                            ))}
                        </SelectContent>

                    </Select>

                    {props.value && (
                        <p className="text-xs text-gray-500">
                            Selected
                            for {props.weekDay}: <b>{props.value.type}</b> ({props.value.exercises.length} exercises)
                        </p>
                    )}
                </div>
            )}

            {mode === "create" && (
                <div className="border rounded-lg p-2 w-190 bg-background">
                    <CreateWorkoutDay onAdd={handleCreateLocal}/>
                </div>
            )}

            <button
                className="text-sm underline mt-2 mx-auto"
                onClick={() => setMode(m => (m === "create" ? "idle" : "create"))}
            >
                {mode === "create" ?
                    (
                        <>
                            Choose existing
                        </>
                    ) : (
                        <>
                            <CirclePlus className="inline w-10 h-5 text-primary text-lg"/>Create new
                        </>
                    )
                }
            </button>
        </>
    );
}

