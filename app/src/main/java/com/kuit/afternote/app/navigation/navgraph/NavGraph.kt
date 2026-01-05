package com.kuit.afternote.app.navigation.navgraph

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kuit.afternote.feature.dailyrecord.presentation.navgraph.recordNavGraph
import com.kuit.afternote.feature.dailyrecord.presentation.screen.RecordMainScreen
import com.kuit.afternote.feature.dev.presentation.screen.DevModeScreen
import com.kuit.afternote.feature.dev.presentation.screen.ModeSelectionScreen
import com.kuit.afternote.feature.dev.presentation.screen.ScreenInfo
import com.kuit.afternote.feature.mainpage.presentation.screen.AfternoteMainScreen
import com.kuit.afternote.feature.onboarding.presentation.navgraph.OnboardingRoute
import com.kuit.afternote.feature.onboarding.presentation.navgraph.onboardingNavGraph
import com.kuit.afternote.feature.onboarding.presentation.screen.LoginScreen
import com.kuit.afternote.feature.onboarding.presentation.screen.ProfileSettingScreen
import com.kuit.afternote.feature.onboarding.presentation.screen.SignUpScreen
import com.kuit.afternote.feature.onboarding.presentation.screen.SplashScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(navHostController: NavHostController) {
    val devModeScreens = listOf(
        ScreenInfo("메인 화면", "main"),
        ScreenInfo("마음의 기록 화면", "recordMain"),
        ScreenInfo("스플래시 화면", "dev_splash"),
        ScreenInfo("로그인 화면", "dev_login"),
        ScreenInfo("회원가입 화면", "dev_signup"),
        ScreenInfo("프로필 설정 화면", "dev_profile_setting")
    )

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

        recordNavGraph(navHostController)

        // 개발자 모드 화면
        composable("dev") {
            DevModeScreen(
                screens = devModeScreens,
                onScreenClick = { route -> navHostController.navigate(route) }
            )
        }

        // 메인 화면 (개발용)
        composable("main") {
            AfternoteMainScreen()
        }

        //마음의 기록 화면
        composable("recordMain") {
            RecordMainScreen(navController = navHostController)
        }




        // 개발자 모드용 화면들
        composable("dev_splash") {
            SplashScreen(
                onLoginClick = { navHostController.navigate("dev_login") },
                onStartClick = { navHostController.navigate("dev_signup") },
                onCheckClick = {}
            )
        }

        composable("dev_login") {
            LoginScreen(
                onBackClick = { navHostController.popBackStack() },
                onLoginClick = {},
                onSignUpClick = { navHostController.navigate("dev_signup") },
                onFindIdClick = {}
            )
        }

        composable("dev_signup") {
            SignUpScreen(
                onBackClick = { navHostController.popBackStack() },
                onSettingClick = { navHostController.navigate("dev_profile_setting") }
            )
        }

        composable("dev_profile_setting") {
            ProfileSettingScreen(
                onFinishClick = {},
                onBackClick = { navHostController.popBackStack() },
                onAddProfileAvatarClick = {}
            )
        }
    }
}
