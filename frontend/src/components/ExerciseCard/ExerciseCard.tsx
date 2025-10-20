import {useState} from "react";
import type {Exercise} from "../../types/Exercise.ts";

type ExerciseCardProps = {
    exercise: Exercise,
}

export default function ExerciseCard(props: Readonly<ExerciseCardProps>) {
    const [name, setName] = useState(props.exercise.name);
    const [sets, setSets] = useState(props.exercise.sets);
    const [reps, setReps] = useState(props.exercise.reps);

    return(
        <div className="exercise-card">
            <h3>ID: {props.exercise.id}</h3>
            <h2>{name}: {sets}x{reps}</h2>
        </div>
    )
}