import { Route, Routes } from "react-router-dom"
import Navbar from "./components/NavBar.tsx"
import ExerciseList from "./components/ExerciseCard/ExerciseList.tsx"
import MainPage from "./components/MainPage/MainPage.tsx"
import WorkoutDaysPage from "./components/WorkoutDayCard/WorkoutDaysPage.tsx"
import WorkoutPlanPage from "./components/WorkoutPlanCard/WorkoutPlanPage.tsx"
import CreateWorkoutPlan from "./components/WorkoutPlanCard/CreateWorkoutPlan.tsx"
import LoginPage from "./components/LoginPage/LoginPage.tsx"
import { FeaturesSection } from "@/components/MainPage/FeatureSection.tsx"
import { CTASection } from "@/components/MainPage/CTASection.tsx"

function App() {
    return (
        <div className="h-full flex flex-col min-h-screen">
            <Navbar />

            {/* Routes should be here at top-level */}
            <div className="flex-grow">
                <Routes>
                    <Route
                        path="/"
                        element={
                            <>
                                <section className="relative min-h-screen">
                                    <div className="relative z-10 max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 pt-20">
                                        <div className="text-center space-y-6">
                                            <MainPage />
                                        </div>
                                    </div>
                                    <div className="absolute inset-0 z-0">
                                        <img
                                            src="/athletic-person-working-out-in-dark-gym-with-drama.jpg"
                                            alt="Gym background"
                                            className="w-full h-full object-cover opacity-70"
                                        />
                                        <div className="absolute inset-0 bg-gradient-to-b from-background/70 via-background/60 to-background" />
                                    </div>
                                </section>
                                <FeaturesSection />
                                <CTASection />
                            </>
                        }
                    />
                    <Route path="/exercises" element={<ExerciseList />} />
                    <Route path="/workouts" element={<WorkoutDaysPage />} />
                    <Route path="/plans" element={<WorkoutPlanPage />} />
                    <Route path="/creatNewPlan" element={<CreateWorkoutPlan />} />
                    <Route path="/login" element={<LoginPage />} />
                </Routes>
            </div>

            <footer className="bg-card border-t border-border py-8 mt-10">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center text-muted-foreground">
                    <p className="text-sm">© 2025 IronForge Gym. All rights reserved.</p>
                </div>
            </footer>
        </div>
    )
}

export default App
