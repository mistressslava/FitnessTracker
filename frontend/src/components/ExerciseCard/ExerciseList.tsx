import ExerciseCard from "./ExerciseCard.tsx";
import {useExercises} from "./UseExercises.ts";
import CreateExercise from "./CreateExercise.tsx";
import {useState} from "react";
import type {Exercise} from "../../types/Exercise.ts";
import "./ExerciseList.css"

export default function ExerciseList() {

    const {exercises, setExercises, loading, error, getAllExercises} = useExercises();
    const [isAdding, setIsAdding] = useState(false);

    function handleAdd(newExercise: Exercise) {
        setExercises(prev => [newExercise, ...prev]);
        getAllExercises();
        setIsAdding(false);
    }

    function handleDelete() {
        getAllExercises();
        setIsAdding(false);
    }

    function handleUpdate(updatedExercise: Exercise) {
        setExercises(prev =>
            prev.map(e => e.id === updatedExercise.id ? updatedExercise : e)
        );
    }


    if (loading) return <p>Loading...</p>;
    if (error) return <p>Failed to load exercises.</p>;

    return (
        <div>
            <h2>Your exercise library: </h2>

            <button
                onClick={() => setIsAdding(prev => !prev)}
                className="bg-indigo-600 text-black px-4 py-2 rounded-xl hover:bg-indigo-700 mb-4"
            >
                {isAdding ? "Cancel" : "Add New Exercise"}
            </button>

            {isAdding && <CreateExercise onAdd={handleAdd}/>}

            <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
                {exercises.map(ex => <ExerciseCard key={ex.id} exercise={ex} handleDeleteExercise={handleDelete}
                                                   onUpdate={handleUpdate}/>)}
            </div>
        </div>
    )
}