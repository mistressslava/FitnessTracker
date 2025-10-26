import type {ExerciseDto} from "@/types/ExerciseDto.ts";
import {type FormEvent, useState} from "react";
import axios from "axios";
import type {Exercise} from "@/types/Exercise.ts";
import {MUSCLE_GROUPS, type MuscleGroup} from "@/types/MuscleGroup.ts";
import {Label} from "@radix-ui/react-label";
import {Input} from "@/components/ui/input.tsx";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select";
import { List } from 'lucide-react';

type NewExerciseProps = {
    onAdd: (newExercise: Exercise) => void
}

export default function CreateExercise({onAdd}: Readonly<NewExerciseProps>) {
    const [exerciseDto, setExerciseDto] = useState<ExerciseDto>({name: "", sets: 0, reps: 0, muscleGroup: undefined});
    const [error, setError] = useState("");

    function onAddNewExercise(e: FormEvent) {
        e.preventDefault();

        axios.post('/api/exercises', exerciseDto)
            .then((res) => {
                onAdd(res.data);
                setExerciseDto({name: "", sets: 0, reps: 0, muscleGroup: undefined});
                setError("");
            })
            .catch(err => {
                if (err.response && typeof err.response.data === "string") {
                    setError(err.response.data)
                } else {
                    setError("Unexpected error occurred");
                }
                console.log(err)
            });
    }

    return (
        <form onSubmit={onAddNewExercise}
              className="flex flex-col justify-center mx-auto gap-3 p-4 border rounded-2xl shadow h-auto w-auto">

            <div className="space-y-4">
                <Label htmlFor="name" className="text-foreground font-medium">
                    Name:
                </Label>
                <Input
                    id="name"
                    type="text"
                    placeholder="e.g. , Bench Press"
                    value={exerciseDto.name}
                    onChange={e => setExerciseDto({...exerciseDto, name: e.target.value})}
                    className={'w-80 h-12 border border-border ${error.includes("Name") ? "border-red-600 border" : ""}'}
                />
            </div>

            <div className="space-y-4">
                <Label htmlFor="name" className="text-foreground font-medium">
                    Sets:
                </Label>
                <Input
                    type="number"
                    value={exerciseDto.sets}
                    onChange={e => setExerciseDto({...exerciseDto, sets: Number(e.target.value)})}
                    className={'w-80 h-12 border-border ${error.includes("Reps") ? "border-red-600 border" : ""}'}
                    required
                />
            </div>

            <div className="space-y-4">
                <Label htmlFor="name" className="text-foreground font-medium">
                    Reps:
                </Label>
                <Input
                    type="number" min={1}
                    value={exerciseDto.reps}
                    onChange={e => setExerciseDto({...exerciseDto, reps: Number(e.target.value)})}
                    className={'w-80 h-12 border-border ${error.includes("Sets") ? "border-red-600 border" : ""}'}
                    required
                />
            </div>


            <div className="relative flex justify-center">
                <Select
                    value={exerciseDto.muscleGroup}
                    onValueChange={(val: MuscleGroup) =>
                        setExerciseDto({...exerciseDto, muscleGroup: val})
                    }
                    required
                >
                    <SelectTrigger
                        id="muscleGroup"
                        className={`w-80 h-12 bg-background text-foreground flex items-center gap-2 border border-border 
                        focus:ring-primary focus:border-primary
                            ${error.includes("Muscle") ? "border-red-600" : ""}`}
                    >
                        <List className="w-5 h-5 text-primary" />
                        <SelectValue placeholder="Select a muscle group"/>
                    </SelectTrigger>

                    <SelectContent
                    position="popper"
                    side="right"
                        className="w-80 bg-background border border-border">
                        {Object.values(MUSCLE_GROUPS).map((group) => (
                            <SelectItem key={group} value={group}>
                                {group}
                            </SelectItem>
                        ))}
                    </SelectContent>
                </Select>
            </div>

            {error && <span className="text-red-600 text-sm">{error}</span>}

            <button type="submit"
                    className="w-80 bg-primary text-primary-foreground px-4 py-2 rounded-xl hover:bg-primary/90 mx-auto">
                Add Exercise
            </button>
        </form>
    )

}