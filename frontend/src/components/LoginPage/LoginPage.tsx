import LoginForm from "@/components/LoginPage/LoginForm.tsx";
import RegisterForm from "@/components/LoginPage/RegisterForm.tsx";
import {useState} from "react";

export default function AuthPage() {

    const [isLogin, setIsLogin] = useState(true);

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