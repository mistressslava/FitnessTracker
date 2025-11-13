import Navbar from "./components/NavBar.tsx";
import ExerciseList from "./components/ExerciseCard/ExerciseList.tsx";
import {Route, Routes, useNavigate} from "react-router-dom";
import MainPage from "./components/MainPage/MainPage.tsx";
import WorkoutDaysPage from "./components/WorkoutDayCard/WorkoutDaysPage.tsx";
import WorkoutPlanPage from "./components/WorkoutPlanCard/WorkoutPlanPage.tsx";
import CreateWorkoutPlan from "./components/WorkoutPlanCard/CreateWorkoutPlan.tsx";
import LoginPage from "./components/LoginPage/LoginPage.tsx";
import axios from "axios";
import {useEffect, useState} from "react";
import ProtectedRoute from "@/components/ProtectedRoutes.tsx";
import type {Users} from "@/types/Users.ts";

function App() {
    const [user, setUser] = useState<Users | undefined | null>(undefined);
    console.log(user);

    const loadUser = () => {
        axios.get("/api/auth/me")
            .then((res) => setUser(res.data))
            .catch((e) => {
                setUser(null);
                console.log(e);
            })
    }

    const nav = useNavigate();

    const api = window.location.host === "localhost:5173"
        ? "http://localhost:8080"
        : window.location.origin;

    const handleLogout = () => {
        axios.post(api + "/logout", null, { withCredentials: true })
            .then( () => {
                console.log("Logged out on backend")
            })
            .catch((err) => {
                console.warn("Logout request failed", err);
            })
            .finally(() => {
                console.log(user);
                localStorage.removeItem("authToken");
                setUser(null);
                nav("/login");
            });
    };

    useEffect(() => {
        loadUser();
    }, []);

    return (
        <div className="h-full">
            <div className="flex flex-col min-h-screen">
                <Navbar user={user} onLogout={handleLogout}/>
                <div className="flex-grow">
                    <section className="relative min-h-screen"> {/*pb-32 overflow-hidden*/}
                        <div className="relative z-10 max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 pt-20">
                            <div className="text-center space-y-6">
                                <Routes>
                                    <Route path={"/"} element={<MainPage user={user}/>}/>
                                    <Route path={"/login"} element={<LoginPage user={user}/>}/>
                                    <Route element={<ProtectedRoute user={user}/>}>
                                        <Route path={"/exercises"} element={<ExerciseList/>}/>
                                        <Route path={"/workouts"} element={<WorkoutDaysPage/>}/>
                                        <Route path={"/plans"} element={<WorkoutPlanPage/>}/>
                                        <Route path={"/creatNewPlan"} element={<CreateWorkoutPlan/>}/>
                                    </Route>
                                </Routes>
                            </div>
                        </div>
                        {/* Background Image with Overlay */}
                        <div className="absolute inset-0 z-0">
                            <img
                                src="/athletic-person-working-out-in-dark-gym-with-drama.jpg"
                                alt="Gym background"
                                className="w-full h-full object-cover opacity-70"
                            />
                            <div
                                className="absolute inset-0 bg-gradient-to-b from-background/70 via-background/60 to-background"/>
                        </div>
                    </section>
                </div>

                <footer className="bg-card border-t border-border py-8 mt-10">
                    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                        <div className="text-center text-muted-foreground">
                            <p className="text-sm">© 2025 IronForge Gym. All rights reserved.</p>
                        </div>
                    </div>
                </footer>
            </div>
        </div>
    )
}

export default App
