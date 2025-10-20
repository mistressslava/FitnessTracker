import Navbar from "./components/NavBar.tsx";
import AllExercises from "./components/ExerciseCard/AllExercises.tsx";
import {Route, Routes} from "react-router-dom";
import MainPage from "./components/MainPage.tsx";

function App() {

  return (
      <div>
          <Navbar/>
          <Routes>
              <Route path={"/"} element={<MainPage/>}/>
              <Route path={"/exercises"} element={<AllExercises/>}/>
          </Routes>
      </div>
  )
}

export default App
