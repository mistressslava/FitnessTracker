import {useEffect, useState} from "react";
import axios from "axios";
import type {WorkoutDay} from "../../types/WorkoutDay.ts";

export function useWorkoutDays() {
    const [days, setDays] = useState<WorkoutDay[]>([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<unknown>(null);

    useEffect(() => {
        setLoading(true);
        axios.get("/api/workout-days")
            .then(res => setDays(res.data))
            .catch(setError)
            .finally(() => setLoading(false));
    }, []);


    function addToLibrary(newDay: WorkoutDay) {
        setDays(prev => [newDay, ...prev]);
    }

    return { days, loading, error, addToLibrary };
}