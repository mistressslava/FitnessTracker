import axios from "axios";
import {useState} from "react";

type DeleteExerciseProps = {
    id: string;
    onDelete: (id: string) => void;
}

export default function DeleteExercise(props: Readonly<DeleteExerciseProps>) {

    const [error, setError] = useState<string>("");

    function handleDeleteExercise() {
        console.log("Trying to delete ID: ", props.id)
        setError("");

        axios.delete('/api/exercises/' + props.id)
            .then(() => {
                props.onDelete(props.id);
            })
            .catch(err => {
                setError(err.response.data);
                console.log(err)
            });
    }

    return (
        <>
            <button onClick={handleDeleteExercise}
                    className="btn bg-destructive text-secondary-foreground rounded-lg hover:bg-destructive/80 transition-colors test-lg border border-border"
            >Delete</button>

            {error && <p className="text-red-600 mt-1">{error}</p>}
        </>
    )
}