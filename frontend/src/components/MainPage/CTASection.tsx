import {useNavigate} from "react-router-dom";

export function CTASection() {

    const navigate = useNavigate();

    return (
        <section className="py-24 bg-background relative overflow-hidden">
            <div className="absolute inset-0 bg-gradient-to-r from-primary/10 via-transparent to-primary/10"/>

            <div className="relative z-10 max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
                <h2 className="text-4xl md:text-5xl lg:text-6xl font-bold text-foreground mb-6 text-balance">
                    Ready to Forge Your <span className="text-primary">Strength</span>?
                </h2>

                <p className="text-lg md:text-xl text-muted-foreground mb-10 max-w-2xl mx-auto leading-relaxed">
                    Join thousands of athletes and fitness enthusiasts who are already transforming their bodies and
                    minds with
                    personalized, science-backed training.
                </p>

                <div className="flex flex-col sm:flex-row gap-4 justify-center">
                    <button
                        className="px-8 py-4 bg-primary text-primary-foreground font-bold rounded-lg hover:bg-primary/90 transition-colors text-lg"
                        onClick={() => navigate("/login")}>
                        Join Now
                    </button>
                    <button
                        className="px-8 py-4 bg-secondary text-secondary-foreground font-bold rounded-lg hover:bg-secondary/80 transition-colors text-lg border border-border"
                        onClick={() => navigate("/login")}>
                        Get Started →
                    </button>
                </div>

                <p className="text-sm text-muted-foreground mt-8">
                    No credit card required. Start your free personalized program today.
                </p>
            </div>
        </section>
    )
}
