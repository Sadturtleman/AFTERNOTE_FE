package com.kuit.afternote.app.navigation.navigator

import androidx.navigation.NavController
import com.kuit.afternote.core.navigation.AfternoteNavigator
import com.kuit.afternote.feature.onboarding.presentation.navgraph.OnboardingRoute

class AfternoteNavigatorImpl(
    private val navController: NavController
) : AfternoteNavigator {
    override fun goToOnboarding(route: OnboardingRoute) {
        navController.navigate(route)
    }
}
