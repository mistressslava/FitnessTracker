import {useState} from "react";
import type {Exercise} from "../../types/Exercise.ts";
import DeleteExercise from "./DeleteExercise.tsx";
import axios from "axios";

type ExerciseCardProps = {
    handleDeleteExercise: (id: string) => void;
    exercise: Exercise;
}

export default function ExerciseCard(props: Readonly<ExerciseCardProps>) {
    const [draftName, setDraftName] = useState(props.exercise.name);
    const [draftSets, setDraftSets] = useState(props.exercise.sets);
    const [draftReps, setDraftReps] = useState(props.exercise.reps);

    const [isEditing, setIsEditing] = useState(false);

    //UPDATE
    function updateExercise() {
        axios.put('api/exercises/' + props.exercise.id, {
            name: draftName,
            sets: draftSets,
            reps: draftReps
        })
            .then(response => {
                setDraftName(response.data.name);
                setDraftSets(response.data.sets);
                setDraftReps(response.data.reps);
            })
            .catch(e => console.log(e))
    }

    function cancel() {
        setDraftName(props.exercise.name);
        setDraftSets(props.exercise.sets);
        setDraftReps(props.exercise.reps);
        setIsEditing(false);
    }

    return (
        <div className="p-3 bg-white rounded-xl shadow-sm border border-gray-200">
            <h3 className="text-sm text-gray-500 font-mono tracking-tight">
                ID: {props.exercise.id}</h3>

            {!isEditing && <h2 className="text-lg font-semibold text-gray-800 mt-1">
                {props.exercise.name}: {props.exercise.sets}x{props.exercise.reps}</h2>}

            {isEditing && <input type={"text"} value={draftName}
                                 onChange={e =>
                                     setDraftName(e.target.value)}
                                 placeholder="Name"/>}
            {isEditing && <input type={'number'} value={draftSets}
                                 onChange={e =>
                                     setDraftSets(Number(e.target.value))}/>}
            {isEditing && <input type={"number"} value={draftReps}
                                 onChange={e =>
                                     setDraftReps(Number(e.target.value))}/>}

            {!isEditing && <button onClick={() => {
                setIsEditing(!isEditing)
            }}>Edit</button>}

            {isEditing && <button onClick={() => {
                updateExercise();
                setIsEditing(!isEditing)
            }}>Save</button>}

            {isEditing && <button onClick={cancel}>Cancel</button>}

            <DeleteExercise id={props.exercise.id} onDelete={props.handleDeleteExercise}/>
        </div>
    )
}