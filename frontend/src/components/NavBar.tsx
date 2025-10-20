import { Link } from "react-router-dom";


export default function Navbar() {

    return (
        <header className="header">
            <nav className="navbar">
                <Link className="nav-button" to={"/"}>Main page</Link>
                <Link className="nav-button" to={"/home"}>Home</Link>
                <Link className="nav-button" to={"/exercises"}>Exercises</Link>
            </nav>
        </header>
    )
}