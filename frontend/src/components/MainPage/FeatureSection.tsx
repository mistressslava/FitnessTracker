import {Dumbbell, PlusCircle, Edit2, TrendingUp, Zap, Lock, Brain} from "lucide-react"

const features = [
    {
        icon: Dumbbell,
        title: "Create Custom Exercises",
        description: "Build your own exercise library with custom names, sets, and muscle group targeting",
    },
    {
        icon: PlusCircle,
        title: "Plan Your Workouts",
        description: "Organize exercises into workout days and create personalized training plans",
    },
    {
        icon: Edit2,
        title: "Edit & Manage Plans",
        description: "Easily modify, update, and delete your exercises and workout plans anytime",
    },
    {
        icon: Brain,
        title: "AI-Generated Workout Plans",
        description: "Tailored for your goals and experience level with continuous optimization",
    }
]

const comingSoonFeatures = [
    {
        icon: TrendingUp,
        title: "Progress Tracking",
        description: "Monitor your improvements over time with detailed analytics and insights",
    },
    {
        icon: Zap,
        title: "Community Challenges",
        description: "Compete, motivate, and grow stronger together with fellow athletes",
    },
    {
        icon: Lock,
        title: "Nutrition Guidance",
        description: "Personalized meal plans designed to fuel your performance and recovery",
    },
]

export function FeaturesSection() {
    return (
        <section className="py-24 bg-card border-y border-border">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <div className="text-center mb-16">
                    <h2 className="text-4xl md:text-5xl font-bold text-foreground mb-4 text-balance">
                        What We <span className="text-primary">Offer</span>
                    </h2>
                    <p className="text-lg text-muted-foreground max-w-2xl mx-auto">
                        Everything you need to transform your body and achieve peak performance
                    </p>
                </div>

                {/* Current Features */}
                <div className="grid grid-cols-1 md:grid-cols-2 gap-8 mb-16">
                    {features.map((feature, index) => {
                        const Icon = feature.icon
                        return (
                            <div
                                key={index}
                                className="bg-background border border-border rounded-lg p-8 hover:border-primary transition-colors hover:shadow-lg hover:shadow-primary/10"
                            >
                                <div className="mb-4">
                                    <div className="bg-primary/20 w-12 h-12 rounded-lg flex items-center justify-center mb-4">
                                        <Icon className="h-6 w-6 text-primary" />
                                    </div>
                                    <h3 className="text-lg font-bold text-foreground">{feature.title}</h3>
                                </div>
                                <p className="text-muted-foreground text-sm leading-relaxed">{feature.description}</p>
                            </div>
                        )
                    })}
                </div>

                <div className="mt-20 pt-16 border-t border-border">
                    <div className="text-center mb-12">
                        <h3 className="text-2xl md:text-3xl font-bold text-foreground mb-2 text-balance">
                            Coming <span className="text-primary">Soon</span>
                        </h3>
                        <p className="text-muted-foreground">Features in development to enhance your experience</p>
                    </div>

                    <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
                        {comingSoonFeatures.map((feature, index) => {
                            const Icon = feature.icon
                            return (
                                <div
                                    key={index}
                                    className="bg-background border border-border/50 rounded-lg p-8 opacity-75 relative overflow-hidden"
                                >
                                    <div className="absolute inset-0 bg-gradient-to-br from-muted-foreground/5 to-transparent pointer-events-none" />
                                    <div className="mb-4 relative z-10">
                                        <div className="bg-muted w-12 h-12 rounded-lg flex items-center justify-center mb-4">
                                            <Icon className="h-6 w-6 text-muted-foreground" />
                                        </div>
                                        <h3 className="text-lg font-bold text-foreground">{feature.title}</h3>
                                    </div>
                                    <p className="text-muted-foreground text-sm leading-relaxed relative z-10">{feature.description}</p>
                                    <div className="absolute top-4 right-4 bg-muted px-3 py-1 rounded-full text-xs font-semibold text-muted-foreground">
                                        Coming Soon
                                    </div>
                                </div>
                            )
                        })}
                    </div>
                </div>
            </div>
        </section>
    )
}
