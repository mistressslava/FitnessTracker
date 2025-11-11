export default function MainPage() {

    return (
        <>
            <div className="relative z-10 max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 pt-20">
                <div className="text-center space-y-6">
                    <h1 className="text-5xl md:text-7xl font-bold text-foreground tracking-tight text-balance">
                        FORGE YOUR <span className="text-primary">STRENGTH</span>
                    </h1>
                    <p className="text-xl md:text-2xl text-muted-foreground max-w-3xl mx-auto text-balance">
                        Transform your body and mind with creating elite training programs
                    </p>
                </div>
            </div>
            <div className="mt-8 p-8 bg-card/80 backdrop-blur rounded-lg max-w-4xl mx-auto border border-border">
                <p className="text-xl text-foreground font-semibold mb-4">💪 About IronForge</p>
                <p className="text-muted-foreground text-xl leading-relaxed mb-4">
                    IronForge is a smart fitness platform that helps you design and follow personalized workout plans —
                    powered by AI technology. Whether you're a beginner or an experienced athlete, our system adapts to your
                    goals, fitness level, and schedule. You can create your own workout plans or let our AI generate
                    structured, science-based routines to help you build strength, improve endurance, and stay consistent.
                </p>
                <p className="text-primary font-semibold text-lg">
                    Train smarter, not harder — every rep, every day, built just for you.
                </p>
            </div>
        </>
    )
}