package com.kuit.afternote.app.navigation.navigator

import androidx.navigation.NavController
import com.kuit.afternote.core.navigation.MainPageNavigator
import com.kuit.afternote.feature.onboarding.presentation.navgraph.OnboardingRoute

class MainPageNavigatorImpl(
    private val navController: NavController
) : MainPageNavigator {
    override fun goToOnboarding(route: OnboardingRoute) {
        navController.navigate(route)
    }
}
