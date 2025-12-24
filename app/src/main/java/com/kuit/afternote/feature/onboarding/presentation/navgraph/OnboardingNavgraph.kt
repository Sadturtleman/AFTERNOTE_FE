package com.kuit.afternote.feature.onboarding.presentation.navgraph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kuit.afternote.feature.onboarding.presentation.screen.LoginScreen
import com.kuit.afternote.feature.onboarding.presentation.screen.ProfileSettingScreen
import com.kuit.afternote.feature.onboarding.presentation.screen.SignUpScreen
import com.kuit.afternote.feature.onboarding.presentation.screen.SplashScreen

fun NavGraphBuilder.onboardingNavGraph(navController: NavController) {
    composable<OnboardingRoute.LoginRoute> {
        LoginScreen(
            onBackClick = { navController.popBackStack() },
            onLoginClick = {},
            onSignUpClick = { navController.navigate(OnboardingRoute.SignUpRoute) },
            onFindIdClick = {}
        )
    }

    composable<OnboardingRoute.SignUpRoute> {
        SignUpScreen(
            onBackClick = { navController.popBackStack() },
            onSettingClick = { navController.navigate(OnboardingRoute.ProfileSettingRoute) }
        )
    }

    composable<OnboardingRoute.SplashRoute> {
        SplashScreen(
            onLoginClick = { navController.navigate(OnboardingRoute.LoginRoute) },
            onCheckClick = {},
            onStartClick = {}
        )
    }

    composable<OnboardingRoute.ProfileSettingRoute> {
        ProfileSettingScreen(
            onFinishClick = { },
            onBackClick = { navController.popBackStack() }
        )
    }
}
