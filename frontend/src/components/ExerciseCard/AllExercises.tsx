import {useEffect, useState} from "react";
import type {Exercise} from "../../types/Exercise.ts";
import axios from "axios";
import ExerciseCard from "./ExerciseCard.tsx";

export default function AllExercises() {
    const [exercises, setExercises] = useState<Exercise[]>([]);

    function getAllExercises() {
        axios.get('api/exercises').then(ex => setExercises(ex.data))
            .catch(e => console.log(e))
    }

    useEffect(() => {
        getAllExercises()
    }, []);

    return (
        <>
            <h2>Your exercise library: </h2>
            <div className="exercise-column">
                {exercises.map(ex => <ExerciseCard key={ex.id} exercise={ex}/>)}
            </div>
        </>
    )
}