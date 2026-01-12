package com.kuit.afternote.app.navigation.navgraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kuit.afternote.feature.dev.presentation.screen.DevModeScreen
import com.kuit.afternote.feature.dev.presentation.screen.ModeSelectionScreen
import com.kuit.afternote.feature.dev.presentation.screen.ScreenInfo
import com.kuit.afternote.feature.mainpage.presentation.screen.AfternoteDetailScreen
import com.kuit.afternote.feature.mainpage.presentation.screen.AfternoteEditScreen
import com.kuit.afternote.feature.mainpage.presentation.screen.AfternoteMainScreen
import com.kuit.afternote.feature.mainpage.presentation.screen.FingerprintLoginScreen
import com.kuit.afternote.feature.onboarding.presentation.navgraph.OnboardingRoute
import com.kuit.afternote.feature.onboarding.presentation.navgraph.onboardingNavGraph
import com.kuit.afternote.feature.onboarding.presentation.screen.LoginScreen
import com.kuit.afternote.feature.onboarding.presentation.screen.ProfileSettingScreen
import com.kuit.afternote.feature.onboarding.presentation.screen.SignUpScreen
import com.kuit.afternote.feature.onboarding.presentation.screen.SplashScreen
import com.kuit.afternote.feature.timeletter.presentation.navgraph.TimeLetterRoute
import com.kuit.afternote.feature.timeletter.presentation.navgraph.timeLetterNavGraph

@Composable
fun NavGraph(navHostController: NavHostController) {
    val devModeScreens = listOf(
        ScreenInfo("메인 화면 (빈 상태)", "main_empty"),
        ScreenInfo("메인 화면 (목록 있음)", "main_with_items"),
        ScreenInfo("애프터노트 상세 화면", "afternote_detail"),
        ScreenInfo("애프터노트 수정 화면", "afternote_edit"),
        ScreenInfo("스플래시 화면", "dev_splash"),
        ScreenInfo("로그인 화면", "dev_login"),
        ScreenInfo("회원가입 화면", "dev_signup"),
        ScreenInfo("프로필 설정 화면", "dev_profile_setting"),
        ScreenInfo("지문 로그인 화면", "fingerprint_login"),
        ScreenInfo("타임레터 화면", "time_letter_main"),
        ScreenInfo("타임레터 작성 화면", "time_letter_writer"),
        ScreenInfo("임시저장 화면", "draft_letter"),
        ScreenInfo("수신자 목록 화면", "receive_list")
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

        // 개발자 모드 화면
        composable("dev") {
            DevModeScreen(
                screens = devModeScreens,
                onScreenClick = { route ->
                    when (route) {
                        "time_letter_main" -> navHostController.navigate(TimeLetterRoute.TimeLetterMainRoute)
                        "time_letter_writer" -> navHostController.navigate(TimeLetterRoute.TimeLetterWriterRoute)
                        "draft_letter" -> navHostController.navigate(TimeLetterRoute.DraftLetterRoute)
                        "receive_list" -> navHostController.navigate(TimeLetterRoute.ReceiveListRoute)
                        else -> navHostController.navigate(route)
                    }
                }
            )
        }

        // 메인 화면 - 빈 상태 (개발용)
        composable("main_empty") {
            AfternoteMainScreen(
                afternoteItems = emptyList(),
                onItemClick = { navHostController.navigate("afternote_detail") }
            )
        }

        // 메인 화면 - 목록 있음 (개발용)
        composable("main_with_items") {
            AfternoteMainScreen(
                afternoteItems = listOf(
                    "인스타그램" to "2023.11.24",
                    "갤러리" to "2023.11.25",
                    "갤러리" to "2023.11.26",
                    "인스타그램" to "2023.11.27"
                ),
                onItemClick = { navHostController.navigate("afternote_detail") }
            )
        }

        // 애프터노트 상세 화면
        composable("afternote_detail") {
            AfternoteDetailScreen(
                onBackClick = { navHostController.popBackStack() },
                onEditClick = { navHostController.navigate("afternote_edit") }
            )
        }

        // 애프터노트 수정 화면
        composable("afternote_edit") {
            AfternoteEditScreen(
                onBackClick = { navHostController.popBackStack() },
                onRegisterClick = { /* TODO: 등록 처리 */ }
            )
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

        // 지문 로그인 화면
        composable("fingerprint_login") {
            FingerprintLoginScreen(
                onFingerprintAuthClick = { /* TODO: 지문 인증 처리 */ }
            )
        }
    }
}
