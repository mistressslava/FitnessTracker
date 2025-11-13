import {FeaturesSection} from "@/components/MainPage/FeatureSection.tsx";
import {CTASection} from "@/components/MainPage/CTASection.tsx";
import {BicepsFlexed} from "lucide-react";
import type {Users} from "@/types/Users.ts";

type NavbarProps = {
    user: Users | null | undefined;
}

export default function MainPage(props: Readonly<NavbarProps>) {

    const isAuthenticated = !!props.user;

    return (
        <>
            <div className="relative z-10 max-w-7xl px-4 sm:px-6 lg:px-8 pt-20">
                <div className="text-center space-y-6">
                    <h1 className="text-5xl md:text-7xl font-bold text-foreground tracking-tight text-balance">
                        FORGE YOUR <span className="text-primary">STRENGTH</span>
                    </h1>
                    <p className="text-xl md:text-2xl text-muted-foreground max-w-3xl mx-auto text-balance">
                        Transform your body and mind with creating elite training programs
                    </p>
                </div>
            </div>
            <div className="mt-8 p-8 pt-4 bg-card/80 backdrop-blur rounded-lg max-w-4xl mx-auto border border-border">
                <p className="text-xl text-foreground font-semibold mb-4 flex items-center justify-center gap-3">
                    <span
                        className="bg-primary/20 w-12 h-12 rounded-lg flex items-center justify-center -translate-y-0.5">
                        <BicepsFlexed className="h-7 w-7 text-primary"/>
                    </span>About IronForge
                </p>

                <p className="text-muted-foreground text-xl leading-relaxed mb-4">
                    IronForge is a smart fitness platform that helps you design and follow personalized workout plans —
                    powered by AI technology. Whether you're a beginner or an experienced athlete, our system adapts to
                    your
                    goals, fitness level, and schedule. You can create your own workout plans or let our AI generate
                    structured, science-based routines to help you build strength, improve endurance, and stay
                    consistent.
                </p>
                <p className="text-primary font-semibold text-lg">
                    Train smarter, not harder — every rep, every day, built just for you.
                </p>
            </div>

            <div
                className="relative left-1/2 right-1/2 -ml-[50vw] -mr-[50vw] w-screen max-w-none overflow-x-hidden
                [&>*]:mx-0 [&>*]:px-0 [&>*]:mt-0 [&>*]:mb-0 pb-0"
            >
                <FeaturesSection/>

                {isAuthenticated ? (
                    []
                ) : (
                    <CTASection/>
                )
                }
            </div>
        </>
    )
}