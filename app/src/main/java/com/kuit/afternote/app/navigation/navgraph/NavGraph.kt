package com.kuit.afternote.app.navigation.navgraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kuit.afternote.feature.dev.presentation.screen.DevModeScreen
import com.kuit.afternote.feature.dev.presentation.screen.ModeSelectionScreen
import com.kuit.afternote.feature.mainpage.presentation.screen.AfternoteMainScreen
import com.kuit.afternote.feature.onboarding.presentation.navgraph.OnboardingRoute
import com.kuit.afternote.feature.onboarding.presentation.navgraph.onboardingNavGraph

@Composable
fun NavGraph(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        startDestination = "mode_selection"
    ) {
        // 모드 선택 화면
        composable("mode_selection") {
            ModeSelectionScreen(
                onUserModeClick = { navHostController.navigate(OnboardingRoute.SplashRoute) },
                onDevModeClick = { navHostController.navigate("dev") }
            )
        }

        onboardingNavGraph(navHostController)

        // 개발자 모드 화면
        composable("dev") {
            DevModeScreen(
                onMainScreenClick = { navHostController.navigate("main") }
            )
        }

        // 메인 화면 (개발용)
        composable("main") {
            AfternoteMainScreen()
        }
    }
}
