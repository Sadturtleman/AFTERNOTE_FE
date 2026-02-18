package com.kuit.afternote.feature.onboarding.presentation.navgraph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kuit.afternote.feature.afternote.presentation.navgraph.AfternoteRoute
import com.kuit.afternote.feature.onboarding.presentation.screen.LoginScreen
import com.kuit.afternote.feature.onboarding.presentation.screen.ProfileSettingScreen
import com.kuit.afternote.feature.onboarding.presentation.screen.SignUpScreen
import com.kuit.afternote.feature.onboarding.presentation.screen.SplashScreen

fun NavGraphBuilder.onboardingNavGraph(
    navController: NavController,
    onNavigateToReceiverOnboarding: () -> Unit
) {
    composable<OnboardingRoute.LoginRoute> {
        LoginScreen(
            onBackClick = { navController.popBackStack() },
            onSignUpClick = { navController.navigate(OnboardingRoute.SignUpRoute) },
            onFindIdClick = {},
            onLoginSuccess = {
                navController.navigate("home") {
                    launchSingleTop = true
                }
            }
        )
    }

    composable<OnboardingRoute.SignUpRoute> {
        SignUpScreen(
            onBackClick = { navController.popBackStack() },
            onSettingClick = { email, password ->
                navController.navigate(
                    OnboardingRoute.ProfileSettingRoute(
                        email = email,
                        password = password
                    )
                )
            }
        )
    }

    composable<OnboardingRoute.SplashRoute> {
        SplashScreen(
            onLoginClick = { navController.navigate(OnboardingRoute.LoginRoute) },
            onCheckClick = onNavigateToReceiverOnboarding,
            onStartClick = { navController.navigate(OnboardingRoute.SignUpRoute) }
        )
    }

    composable<OnboardingRoute.ProfileSettingRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<OnboardingRoute.ProfileSettingRoute>()
        ProfileSettingScreen(
            email = route.email,
            password = route.password,
            onFinishClick = {
                android.util.Log.d("OnboardingNavGraph", "onFinishClick 호출됨, AfternoteListRoute로 이동 시도")
                navController.navigate(AfternoteRoute.AfternoteListRoute)
                android.util.Log.d("OnboardingNavGraph", "navigate 호출 완료")
            },
            onBackClick = { navController.popBackStack() }
        )
    }
}
