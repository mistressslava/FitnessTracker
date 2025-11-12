import {Link} from "react-router-dom";
import {Dumbbell, Menu} from "lucide-react"
import {Button} from "@/components/ui/button.tsx";
import type {Users} from "@/types/Users.ts";
import {useState} from "react";
import {Sheet, SheetContent, SheetTrigger} from "@/components/ui/sheet"

type NavbarProps = {
    user: Users | null | undefined;
    onLogout: () => void;
}

export default function Navbar(props: Readonly<NavbarProps>) {

    const isAuthenticated = !!props.user;

    const [open, setOpen] = useState(false);

    const navLinks = isAuthenticated
        ? [
            {href: "/", label: "Home"},
            {href: "/exercises", label: "Exercises"},
            {href: "/workouts", label: "Workouts"},
            {href: "/plans", label: "Plans"},
            {href: "/creatNewPlan", label: "Create NEW plan"},
        ]
        : []

    return (
        <nav className="fixed top-0 left-0 right-0 z-50 bg-background/80 backdrop-blur-md border-b border-border">
            <div className="max-w-8xl mx-auto px-4 sm:px-6 lg:px-8">
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
                    <div className="flex items-center justify-between">
                        {/* Desktop Navigation */}
                        <div className="hidden md:flex items-center gap-8">
                            {isAuthenticated ? (
                                <>
                                    {navLinks.map((link) => (
                                        <Link
                                            key={link.href}
                                            className="text-sm font-medium text-muted-foreground hover:text-primary transition-colors"
                                            to={link.href}
                                        >
                                            {link.label}
                                        </Link>
                                    ))}
                                    <Button
                                        onClick={props.onLogout}
                                        className="text-sm font-medium bg-destructive hover:bg-destructive/80 text-destructive-foreground"
                                    >
                                        Logout
                                    </Button>
                                </>
                            ) : (
                                <Link
                                    className="text-sm font-medium bg-primary hover:bg-primary/90 text-primary-foreground px-4 py-2 rounded-md transition-colors"
                                    to="/login"
                                >
                                    Login
                                </Link>
                            )}
                        </div>

                        {/* Mobile Navigation */}
                        <div className="md:hidden">
                            <Sheet open={open} onOpenChange={setOpen}>
                                <SheetTrigger asChild>
                                    <Button variant="ghost" size="icon" className="text-muted-foreground">
                                        <Menu className="h-5 w-5"/>
                                        <span className="sr-only">Menu</span>
                                    </Button>
                                </SheetTrigger>
                                <SheetContent side="right" className="w-[250px]">
                                    <div className="flex flex-col gap-4 mt-10">
                                        {isAuthenticated ? (
                                            <>
                                                {navLinks.map((link) => (
                                                    <Link
                                                        key={link.href}
                                                        className="text-sm font-medium text-muted-foreground hover:text-primary transition-colors px-2 py-2"
                                                        to={link.href}
                                                        onClick={() => setOpen(false)}
                                                    >
                                                        {link.label}
                                                    </Link>
                                                ))}
                                                <Button
                                                    onClick={() => {
                                                        props.onLogout();
                                                        setOpen(false);
                                                    }}
                                                    className="mt-4 w-full text-sm font-medium bg-destructive hover:bg-destructive/80 text-destructive-foreground"
                                                >
                                                    Logout
                                                </Button>
                                            </>
                                        ) : (
                                            <Link
                                                className="text-sm font-medium bg-primary hover:bg-primary/90 text-primary-foreground px-4 py-2 rounded-md transition-colors inline-block"
                                                to="/login"
                                                onClick={() => setOpen(false)}
                                            >
                                                Login
                                            </Link>
                                        )}
                                    </div>
                                </SheetContent>
                            </Sheet>
                        </div>
                    </div>
                </div>
            </div>
        </nav>
    );
}