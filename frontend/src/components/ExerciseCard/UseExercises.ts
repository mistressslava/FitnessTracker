import {useEffect, useState} from "react";
import axios from "axios";
import type {Exercise} from "../../types/Exercise.ts";

export function useExercises() {
    const [exercises, setExercises] = useState<Exercise[]>([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<unknown>(null);

    function getAllExercises() {
        setLoading(true);
        axios.get('/api/exercises')
            .then(res => {
                setExercises(res.data)
                setError(null);
            })
            .catch(e => {
                console.log(e);
                setError(e)
            })
            .finally(() => setLoading(false));
    }

    useEffect(() => {
        getAllExercises()
    }, []);

    return { exercises, setExercises, loading, error, getAllExercises }
}