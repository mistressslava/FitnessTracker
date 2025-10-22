import Navbar from "./components/NavBar.tsx";
import ExerciseList from "./components/ExerciseCard/ExerciseList.tsx";
import {Route, Routes} from "react-router-dom";
import MainPage from "./components/MainPage.tsx";
import WorkoutDaysPage from "./components/WorkoutDayCard/WorkoutDaysPage.tsx";

function App() {

  return (
      <div>
          <Navbar/>
          <Routes>
              <Route path={"/"} element={<MainPage/>}/>
              <Route path={"/exercises"} element={<ExerciseList/>}/>
              <Route path={"/workouts"} element={<WorkoutDaysPage/>}/>
          </Routes>
      </div>
  )
}

export default App
