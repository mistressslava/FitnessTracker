import {type FormEvent, useState} from "react";
import axios from "axios";
import {Card, CardContent, CardDescription, CardHeader, CardTitle} from "@/components/ui/card.tsx";
import {Input} from "@/components/ui/input.tsx";
import {Button} from "@/components/ui/button.tsx";
import {Chrome, GithubIcon} from "lucide-react";

type RegisterFormProps = {
    onSwitchToLogin: () => void
}

export default function RegisterForm(props: Readonly<RegisterFormProps>) {
    const [username, setUsername] = useState("")
    const [password, setPassword] = useState("")
    const [confirmPassword, setConfirmPassword] = useState("")
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState("")

    const handleSubmit = (e: FormEvent) => {
        e.preventDefault()
        setError("")

        if (password !== confirmPassword) {
            setError("Passwords do not match")
            return;
        }

        setLoading(true)

        axios.post("/api/auth/register", { username, password })
            .then(res => {
                const token = res.data?.token;
                if (!token) throw new Error("No token in response");
                localStorage.setItem("authToken", token);
                setUsername(""); setPassword(""); setConfirmPassword(""); setError("");
                window.location.href = "/";
            })
            .catch((err) => {
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
                setLoading(false)
            })
    }

    const handleOAuthRegister = (provider: "github" | "google") => {
        window.location.href = `/oauth2/authorization/${provider}`;
    }

    return (
        <Card className="border-border bg-card">
            <CardHeader className="space-y-2">
                <CardTitle className="text-2xl text-foreground">Create Account</CardTitle>
                <CardDescription className="text-muted-foreground">Sign up for a new account</CardDescription>
            </CardHeader>
            <CardContent className="space-y-4">
                <form onSubmit={handleSubmit} className="space-y-4">
                    <div className="space-y-2">
                        <label htmlFor="username" className="text-sm font-medium text-foreground">
                            Username
                        </label>
                        <Input
                            id="username"
                            type="text"
                            placeholder="johndoe"
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

                    <div className="space-y-2">
                        <label htmlFor="confirmPassword" className="text-sm font-medium text-foreground">
                            Confirm Password
                        </label>
                        <Input
                            id="confirmPassword"
                            type="password"
                            placeholder="••••••••"
                            value={confirmPassword}
                            onChange={(e) => setConfirmPassword(e.target.value)}
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
                        {loading ? "Creating account..." : "Sign Up"}
                    </Button>
                </form>

                <div className="relative">
                    <div className="absolute inset-0 flex items-center">
                        <div className="w-full border-t border-border"></div>
                    </div>
                    <div className="relative flex justify-center text-sm">
                        <span className="px-2 bg-card text-muted-foreground">Or register with</span>
                    </div>
                </div>

                <div className="grid grid-cols-2 gap-3">
                    <Button
                        type="button"
                        variant="outline"
                        onClick={() => handleOAuthRegister("github")}
                        className="border-border hover:bg-secondary text-foreground"
                    >
                        <GithubIcon className="w-4 h-4 mr-2"/>
                        GitHub
                    </Button>
                    <Button
                        type="button"
                        variant="outline"
                        onClick={() => handleOAuthRegister("google")}
                        className="border-border hover:bg-secondary text-foreground"
                    >
                        <Chrome className="w-4 h-4 mr-2"/>
                        Google
                    </Button>
                </div>

                <p className="text-center text-sm text-muted-foreground">
                    Already have an account?{" "}
                    <button onClick={props.onSwitchToLogin} className="text-primary hover:underline font-medium">
                        Sign in
                    </button>
                </p>
            </CardContent>
        </Card>
    )
}
