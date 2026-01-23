package com.kuit.afternote.app.navigation.navgraph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kuit.afternote.app.navigation.navigator.MainPageNavigatorImpl
import com.kuit.afternote.feature.dev.presentation.screen.DevModeScreen
import com.kuit.afternote.feature.dev.presentation.screen.ModeSelectionScreen
import com.kuit.afternote.feature.dev.presentation.screen.ScreenInfo
import com.kuit.afternote.feature.mainpage.presentation.navgraph.MainPageRoute
import com.kuit.afternote.feature.mainpage.presentation.navgraph.mainPageNavGraph
import com.kuit.afternote.feature.mainpage.presentation.screen.AfternoteDetailScreen
import com.kuit.afternote.feature.mainpage.presentation.screen.AfternoteEditScreen
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
    var afternoteItems by remember { mutableStateOf(listOf<Pair<String, String>>()) }
    val mainPageNavigator = remember(navHostController) {
        MainPageNavigatorImpl(navHostController)
    }
    val devModeScreens = listOf(
        ScreenInfo("메인 화면 (빈 상태)", "main_empty"),
        ScreenInfo("메인 화면 (목록 있음)", "main_with_items"),
        ScreenInfo("애프터노트 상세 화면", "afternote_detail"),
        ScreenInfo("애프터노트 수정 화면", "afternote_edit"),
        ScreenInfo("스플래시 화면", "dev_splash"),
        ScreenInfo("로그인 화면", "dev_login"),
        ScreenInfo("회원가입 화면", "dev_signup"),
        ScreenInfo("지문 로그인 화면", "fingerprint_login"),
        ScreenInfo("타임레터 화면", "time_letter_main"),
        ScreenInfo("타임레터 작성 화면", "time_letter_writer"),
        ScreenInfo("임시저장 화면", "draft_letter"),
        ScreenInfo("수신자 목록 화면", "receive_list"),
        ScreenInfo("타임레터 빈 화면", "letter_empty")
    )

    NavHost(
        navController = navHostController,
        startDestination = "dev"
    ) {
        // 모드 선택 화면 (사용하지 않음, 개발자 모드에서 직접 사용자 모드로 이동 가능)
        composable("mode_selection") {
            ModeSelectionScreen(
                onUserModeClick = { navHostController.navigate(OnboardingRoute.SplashRoute) },
                onDevModeClick = { navHostController.navigate("dev") }
            )
        }

        onboardingNavGraph(navHostController)

        mainPageNavGraph(
            navController = navHostController,
            afternoteItems = afternoteItems,
            onItemsUpdated = { afternoteItems = it },
            mainPageNavigator = mainPageNavigator
        )

        timeLetterNavGraph(
            navController = navHostController
        )

        // 개발자 모드 화면 (기본 시작 화면)
        composable("dev") {
            DevModeScreen(
                screens = devModeScreens,

                onScreenClick = { route ->

                // 문자열 route를 MainPageRoute로 변환하여 navigate
                when (route) {
                    "main_empty" -> navHostController.navigate(MainPageRoute.MainEmptyRoute)
                    "main_with_items" -> navHostController.navigate(MainPageRoute.MainWithItemsRoute)
                    "afternote_detail" -> navHostController.navigate(MainPageRoute.DetailRoute)
                    "afternote_edit" -> navHostController.navigate(MainPageRoute.EditRoute)
                    "fingerprint_login" -> navHostController.navigate(MainPageRoute.FingerprintLoginRoute)
                    "time_letter_main" -> navHostController.navigate(TimeLetterRoute.TimeLetterMainRoute)
                    "time_letter_writer" -> navHostController.navigate(TimeLetterRoute.TimeLetterWriterRoute)
                    "draft_letter" -> navHostController.navigate(TimeLetterRoute.DraftLetterRoute)
                    "receive_list" -> navHostController.navigate(TimeLetterRoute.ReceiveListRoute)
                    "letter_empty" -> navHostController.navigate(TimeLetterRoute.LetterEmptyRoute)
                    else -> navHostController.navigate(route) // 기타 route는 문자열로 처리
                }
            },
            onUserModeClick = { navHostController.navigate(OnboardingRoute.SplashRoute) }

            )
        }

        // 메인 화면 - 빈 상태 (개발용)

        // 애프터노트 상세 화면
        composable("afternote_detail") {
            AfternoteDetailScreen(
                onBackClick = { navHostController.popBackStack() },
                onEditClick = { navHostController.navigate("afternote_edit") }
            )
        }

        // 애프터노트 수정 화면 (개발자 모드용)
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
                onCheckClick = {},
                onSignUpClick = { navHostController.navigate("dev_signup") }
            )
        }

        composable("dev_login") {
            LoginScreen(
                onBackClick = { navHostController.popBackStack() },
                onSignUpClick = { navHostController.navigate("dev_signup") },
                onFindIdClick = {},
                onLoginSuccess = { navHostController.navigate(MainPageRoute.MainEmptyRoute) }
            )
        }

        composable("dev_signup") {
            SignUpScreen(
                onBackClick = { navHostController.popBackStack() },
                onSettingClick = { email, password ->
                    navHostController.navigate("dev_profile_setting/$email/$password")
                }
            )
        }

        composable("dev_profile_setting/{email}/{password}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val password = backStackEntry.arguments?.getString("password") ?: ""
            ProfileSettingScreen(
                email = email,
                password = password,
                onFinishClick = { navHostController.navigate(MainPageRoute.MainEmptyRoute) },
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
