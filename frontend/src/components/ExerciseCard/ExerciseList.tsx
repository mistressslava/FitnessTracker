import ExerciseCard from "./ExerciseCard.tsx";
import {useExercises} from "./UseExercises.ts";
import CreateExercise from "./CreateExercise.tsx";
import {useState} from "react";
import type {Exercise} from "@/types/Exercise.ts";
import {Card} from "@/components/ui/card.tsx";

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
        <Card className="bg-card border-border p-8 max-w-6xl mx-auto">
            <h1 className="text-5xl md:text-5xl font-bold text-foreground tracking-tight text-balance">
                Your <span className="text-primary">EXERCISE</span> library:
            </h1>

            <button
                onClick={() => setIsAdding(prev => !prev)}
                className={`w-80 px-4 py-2 rounded-xl mx-auto transition-colors
                    ${isAdding
                        ? "bg-destructive text-destructive-foreground hover:bg-destructive/80"
                        : "bg-primary text-primary-foreground hover:bg-primary/90"}
                    `}
            >
                {isAdding ? "Cancel" : "Add New Exercise"}
            </button>

            {isAdding && <CreateExercise onAdd={handleAdd}/>}

            <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
                {exercises.map(ex => <ExerciseCard key={ex.id} exercise={ex} handleDeleteExercise={handleDelete}
                                                   onUpdate={handleUpdate}/>)}
            </div>
        </Card>
    )
}