import {Link} from "react-router-dom";
import {Dumbbell} from "lucide-react"

export default function Navbar() {

    return (
        <nav className="fixed top-0 left-0 right-0 z-50 bg-background/80 backdrop-blur-md border-b border-border">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <div className="flex items-center justify-between h-16">
                    {/* Logo */}
                    <Link to="/" className="flex items-center gap-2 group">
                        <div className="bg-primary p-2 rounded-lg transition-transform group-hover:scale-110">
                            <Dumbbell className="h-6 w-6 text-primary-foreground"/>
                        </div>
                        <span className="text-xl font-bold tracking-tight text-foreground">
                            IRON<span className="text-primary">FORGE</span>
                        </span>
                    </Link>

                    {/* Navigation */}
                    <div className="hidden md:flex items-center gap-8">
                        <Link className="text-sm font-medium text-muted-foreground hover:text-primary transition-colors"
                              to={"/"}>
                            Main page
                        </Link>
                        <Link className="text-sm font-medium text-muted-foreground hover:text-primary transition-colors"
                              to={"/home"}>Home
                        </Link>
                        <Link className="text-sm font-medium text-muted-foreground hover:text-primary transition-colors"
                              to={"/exercises"}>Exercises
                        </Link>
                        <Link className="text-sm font-medium text-muted-foreground hover:text-primary transition-colors"
                              to={"/workouts"}>Workouts
                        </Link>
                        <Link className="text-sm font-medium text-muted-foreground hover:text-primary transition-colors"
                              to={"/plans"}>Plans
                        </Link>
                        <Link className="text-sm font-medium text-muted-foreground hover:text-primary transition-colors"
                              to={"/creatNewPlan"}>Create NEW plan
                        </Link>
                    </div>
                </div>
            </div>
        </nav>
    )
}