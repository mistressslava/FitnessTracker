import LoginForm from "@/components/LoginPage/LoginForm.tsx";
import RegisterForm from "@/components/LoginPage/RegisterForm.tsx";
import {useEffect, useState} from "react";
import type {Users} from "@/types/Users.ts";
import {useNavigate} from "react-router-dom";

type NavbarProps = {
    user: Users | null | undefined;
}

export default function AuthPage(props: Readonly<NavbarProps>) {

    const nav = useNavigate();
    const [isLogin, setIsLogin] = useState(true);

    useEffect(() => {
        if (props.user) {
            const timer = setTimeout(() => {
                nav("/");
            }, 5000);

            return () => clearTimeout(timer);
        }
    }, [props.user, nav]);

    if (props.user) {
        return (
            <div className="min-h-screen flex items-center justify-center p-4">
                <p className="text-lg text-muted-foreground">
                    You are already logged in.
                </p>
            </div>
        );
    }

    return (
        <div className="min-h-screen flex items-center justify-center p-4">
            <div className="w-full max-w-md">
                {isLogin ? (
                    <LoginForm onSwitchToRegister={() => setIsLogin(false)} />
                ) : (
                    <RegisterForm onSwitchToLogin={() => setIsLogin(true)} />
                )}
            </div>
        </div>
    )
}