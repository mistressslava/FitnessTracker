import axios from "axios";
import {useState} from "react";

type DeleteExerciseProps = {
    id: string;
    onDelete: (id: string) => void;
}

export default function DeleteExercise(props: DeleteExerciseProps) {

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
                    className="border border-red-500 text-red-600 px-2 py-1 rounded hover:bg-red-50"
            >Delete</button>

            {error && <p className="text-red-600 mt-1">{error}</p>}
        </>
    )
}