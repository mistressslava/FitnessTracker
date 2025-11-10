import {type FormEvent, useState} from "react";
import {Card, CardContent, CardDescription, CardHeader, CardTitle} from "@/components/ui/card.tsx";
import {Input} from "@/components/ui/input.tsx";
import {Button} from "@/components/ui/button.tsx";
import {Chrome, GithubIcon} from "lucide-react";
import axios from "axios";
import type {Users} from "@/types/Users.ts";

type LoginFormProps = {
    onSwitchToRegister: () => void
}

export default function LoginForm(props: Readonly<LoginFormProps>) {
    const [user, setUser] = useState<Users>({ username: "", password: "" })
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    function handleSubmit(e: FormEvent) {
        e.preventDefault();

        setError("");
        setLoading(true);

        const userData = { username, password }

        axios.post('/api/auth/login', userData)
            .then(res => {
                localStorage.setItem("authToken", res.data.token)
                setUser({username: "", password: ""})
                setError("");
                window.location.href = "/"
            })
            .catch(err => {
                if (err.response && typeof err.response.data === "string") {
                    setError(err.response.data)
                } else if (err.response?.data?.message) {
                    setError(err.response.data.message)
                } else {
                    setError("Unexpected error occurred")
                }
                console.log(err)
            })
            .finally(() => {
                setLoading(false);
            });
    }

    const handleOAuthLogin = (provider: "github" | "google") => {
        window.location.href = `/api/oauth/${provider}/login`
    }

    return (
        <Card>
            <CardHeader className="space-y-2">
                <CardTitle className="text-2xl text-foreground">Welcome Back</CardTitle>
                <CardDescription className="text-muted-foreground">Sign in to your account</CardDescription>
            </CardHeader>
            <CardContent className="space-y-4">
                <form onSubmit={handleSubmit} className="space-y-4">
                    <div className="space-y-2">
                        <label htmlFor="email" className="text-sm font-medium text-foreground">
                            Username
                        </label>
                        <Input
                            id="username"
                            type="johndoe"
                            placeholder="username"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            className="bg-input border-border text-foreground placeholder:text-muted-foreground"
                            required
                        />
                    </div>

                    <div className="space-y-2">
                        <label htmlFor="password" className="text-sm font-medium text-foreground">
                            Password
                        </label>
                        <Input
                            id="password"
                            type="password"
                            placeholder="••••••••"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            className="bg-input border-border text-foreground placeholder:text-muted-foreground"
                            required
                        />
                    </div>

                    {error && (
                        <div
                            className="bg-destructive/10 border border-destructive rounded-md p-3 text-sm text-destructive">
                            {error}
                        </div>
                    )}

                    <Button
                        type="submit"
                        disabled={loading}
                        className="w-full bg-primary hover:bg-primary/90 text-primary-foreground font-medium"
                    >
                        {loading ? "Signing in..." : "Sign In"}
                    </Button>
                </form>

                <div className="relative">
                    <div className="absolute inset-0 flex items-center">
                        <div className="w-full border-t border-border"></div>
                    </div>
                    <div className="relative flex justify-center text-sm">
                        <span className="px-2 bg-card text-muted-foreground">Or continue with</span>
                    </div>
                </div>

                <div className="grid grid-cols-2 gap-3">
                    <Button
                        type="button"
                        variant="outline"
                        onClick={() => handleOAuthLogin("github")}
                        className="border-border hover:bg-secondary text-foreground"
                    >
                        <GithubIcon className="w-4 h-4 mr-2"/>
                        GitHub
                    </Button>
                    <Button
                        type="button"
                        variant="outline"
                        onClick={() => handleOAuthLogin("google")}
                        className="border-border hover:bg-secondary text-foreground"
                    >
                        <Chrome className="w-4 h-4 mr-2"/>
                        Google
                    </Button>
                </div>

                <p className="text-center text-sm text-muted-foreground">
                    Don't have an account?{" "}
                    <button onClick={props.onSwitchToRegister} className="text-primary hover:underline font-medium">
                        Sign up
                    </button>
                </p>
            </CardContent>
        </Card>
    )
}