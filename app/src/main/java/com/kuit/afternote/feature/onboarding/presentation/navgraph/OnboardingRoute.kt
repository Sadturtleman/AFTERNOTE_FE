package com.kuit.afternote.feature.onboarding.presentation.navgraph

import kotlinx.serialization.Serializable

sealed interface OnboardingRoute {
    @Serializable
    data object LoginRoute : OnboardingRoute

    @Serializable
    data object SplashRoute : OnboardingRoute

    @Serializable
    data object SignUpRoute : OnboardingRoute

    @Serializable
    data object ProfileSettingRoute : OnboardingRoute
}
