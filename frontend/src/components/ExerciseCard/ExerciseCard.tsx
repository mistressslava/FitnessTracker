import {useState} from "react";
import type {Exercise} from "../../types/Exercise.ts";

type ExerciseCardProps = {
    exercise: Exercise,
}

export default function ExerciseCard(props: Readonly<ExerciseCardProps>) {
    const [name] = useState(props.exercise.name);
    const [sets] = useState(props.exercise.sets);
    const [reps] = useState(props.exercise.reps);

    return(
        <div className="p-3 bg-white rounded-xl shadow-sm border border-gray-200">
            <h3 className="text-sm text-gray-500 font-mono tracking-tight">
                ID: {props.exercise.id}</h3>
            <h2 className="text-lg font-semibold text-gray-800 mt-1">{name}: {sets}x{reps}</h2>
        </div>
    )
}