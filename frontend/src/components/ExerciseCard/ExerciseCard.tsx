import {useState} from "react";
import type {Exercise} from "@/types/Exercise.ts";
import DeleteExercise from "./DeleteExercise.tsx";
import axios from "axios";
import {MUSCLE_GROUPS, type MuscleGroup} from "@/types/MuscleGroup.ts";
import {Card, CardTitle} from "@/components/ui/card.tsx";

type ExerciseCardProps = {
    handleDeleteExercise: (id: string) => void;
    exercise: Exercise;
    onUpdate(updatedExercise: Exercise): void;
}

export default function ExerciseCard(props: Readonly<ExerciseCardProps>) {
    const [draftName, setDraftName] = useState(props.exercise.name);
    const [draftSets, setDraftSets] = useState(props.exercise.sets);
    const [draftReps, setDraftReps] = useState(props.exercise.reps);
    const [draftMuscleGroup, setDraftMuscleGroup] = useState(props.exercise.muscleGroup);

    const [isEditing, setIsEditing] = useState(false);

    //UPDATE
    function updateExercise() {
        axios.put('api/exercises/' + props.exercise.id, {
            name: draftName,
            sets: draftSets,
            reps: draftReps,
            muscleGroup: draftMuscleGroup
        })
            .then(response => {
                setDraftName(response.data.name);
                setDraftSets(response.data.sets);
                setDraftReps(response.data.reps);
                setDraftMuscleGroup(response.data.muscleGroup);

                props.onUpdate?.(response.data);
            })
            .catch(e => console.log(e))
    }

    function cancel() {
        setDraftName(props.exercise.name);
        setDraftSets(props.exercise.sets);
        setDraftReps(props.exercise.reps);
        setDraftMuscleGroup(props.exercise.muscleGroup);
        setIsEditing(false);
    }

    return (
        <Card className="bg-card border-border p-10 max-w-2xl mx-auto">
            {/*<h3 className="text-sm text-gray-500 font-mono tracking-tight">
                ID: {props.exercise.id}</h3>*/}

            {!isEditing && (
                <div className="flex flex-col text-secondary-foreground mx-1">
                    <CardTitle className="text-lg font-semibold">{props.exercise.name}</CardTitle>
                    <p className="text-md">Sets: {props.exercise.sets}</p>
                    <p className="text-md">Reps: {props.exercise.reps}</p>
                    <p className="text-md">Muscle group: {props.exercise.muscleGroup}</p>
                </div>
            )}

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
            {isEditing && <select value={draftMuscleGroup}
                                  onChange={e =>
                                      setDraftMuscleGroup(e.target.value as MuscleGroup)}
            >
                <option value="">Select a muscle group</option>
                {Object.values(MUSCLE_GROUPS).map((group) => (
                    <option key={group} value={group}>
                        {group}
                    </option>
                ))}
            </select>
            }

            <div className="button-group">
                {!isEditing && <button onClick={() => {
                    setIsEditing(!isEditing)
                }}
                                       className="btn bg-secondary text-secondary-foreground rounded-lg hover:bg-secondary/50 transition-colors text-lg border border-border"
                >Edit</button>}

                {isEditing && <button onClick={() => {
                    updateExercise();
                    setIsEditing(!isEditing)
                }}
                                      className="btn bg-primary text-primary-foreground rounded-lg hover:bg-primary/50 transition-colors text-lg border border-border"
                >Save</button>}

                {isEditing && <button onClick={cancel}
                                      className="btn bg-secondary text-secondary-foreground rounded-lg hover:bg-secondary/50 transition-colors text-lg border border-border"

                >Cancel</button>}

                <DeleteExercise id={props.exercise.id} onDelete={props.handleDeleteExercise}/>
            </div>
        </Card>
    )
}