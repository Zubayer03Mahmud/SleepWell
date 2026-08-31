package com.example.sleepwell.ui.onboarding

import androidx.compose.runtime.Composable

data class OnboardingPageData(
    val title: String,
    val description: String,
    val illustration: @Composable () -> Unit
)

val onboardingPages = listOf(
    OnboardingPageData(
        title = "Build Better Sleep Habits",
        description = "Get personalized daily recommendations designed just for you.",
        illustration = { FloatingIllustration { MoonIllustration() } }
    ),
    OnboardingPageData(
        title = "AI Predicts Your Sleep Quality",
        description = "Our advanced AI analyzes your habits and predicts your sleep quality with 94% accuracy.",
        illustration = { FloatingIllustration { RobotIllustration() } }
    ),
    OnboardingPageData(
        title = "Improve Your Sleep",
        description = "Track, analyze, and optimize your sleep patterns with science-backed insights.",
        illustration = { FloatingIllustration { SleepingIllustration() } }
    )
)