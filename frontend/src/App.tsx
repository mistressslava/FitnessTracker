import Navbar from "./components/NavBar.tsx";
import ExerciseList from "./components/ExerciseCard/ExerciseList.tsx";
import {Route, Routes} from "react-router-dom";
import MainPage from "./components/MainPage.tsx";
import WorkoutDaysPage from "./components/WorkoutDayCard/WorkoutDaysPage.tsx";
import WorkoutPlanPage from "./components/WorkoutPlanCard/WorkoutPlanPage.tsx";
import CreateWorkoutPlan from "./components/WorkoutPlanCard/CreateWorkoutPlan.tsx";

function App() {

  return (
      <div>
          <Navbar/>
          <Routes>
              <Route path={"/"} element={<MainPage/>}/>
              <Route path={"/exercises"} element={<ExerciseList/>}/>
              <Route path={"/workouts"} element={<WorkoutDaysPage/>}/>
              <Route path={"/plans"} element={<WorkoutPlanPage/>}/>
              <Route path={"/creatNewPlan"} element={<CreateWorkoutPlan/>}/>
          </Routes>
      </div>
  )
}

export default App
