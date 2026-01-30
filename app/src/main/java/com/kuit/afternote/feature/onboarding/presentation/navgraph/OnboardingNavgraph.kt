package com.kuit.afternote.feature.onboarding.presentation.navgraph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kuit.afternote.feature.mainpage.presentation.navgraph.MainPageRoute
import com.kuit.afternote.feature.onboarding.presentation.screen.LoginScreen
import com.kuit.afternote.feature.onboarding.presentation.screen.ProfileSettingScreen
import com.kuit.afternote.feature.onboarding.presentation.screen.SignUpScreen
import com.kuit.afternote.feature.onboarding.presentation.screen.SplashScreen

fun NavGraphBuilder.onboardingNavGraph(navController: NavController) {
    composable<OnboardingRoute.LoginRoute> {
        LoginScreen(
            onBackClick = { navController.popBackStack() },
            onSignUpClick = { navController.navigate(OnboardingRoute.SignUpRoute) },
            onFindIdClick = {},
            onLoginSuccess = { navController.navigate(MainPageRoute.MainRoute) }
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
            onCheckClick = {},
            onStartClick = {},
            onSignUpClick = { navController.navigate(OnboardingRoute.SignUpRoute) }
        )
    }

    composable<OnboardingRoute.ProfileSettingRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<OnboardingRoute.ProfileSettingRoute>()
        ProfileSettingScreen(
            email = route.email,
            password = route.password,
            onFinishClick = {
                android.util.Log.d("OnboardingNavGraph", "onFinishClick 호출됨, MainRoute로 이동 시도")
                navController.navigate(MainPageRoute.MainRoute)
                android.util.Log.d("OnboardingNavGraph", "navigate 호출 완료")
            },
            onBackClick = { navController.popBackStack() },
            onAddProfileAvatarClick = { }
        )
    }
}
