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
import com.kuit.afternote.feature.onboarding.presentation.navgraph.OnboardingRoute
import com.kuit.afternote.feature.onboarding.presentation.navgraph.onboardingNavGraph
import com.kuit.afternote.feature.onboarding.presentation.screen.LoginScreen
import com.kuit.afternote.feature.onboarding.presentation.screen.ProfileSettingScreen
import com.kuit.afternote.feature.onboarding.presentation.screen.SignUpScreen
import com.kuit.afternote.feature.onboarding.presentation.screen.SplashScreen

@Composable
fun NavGraph(navHostController: NavHostController) {
    // 애프터노트 목록 상태 관리
    var afternoteItems by remember {
        mutableStateOf(
            listOf(
                "인스타그램" to "2023.11.24",
                "갤러리" to "2023.11.25",
                "추모 가이드라인" to "2023.11.26",
                "네이버 메일" to "2023.11.27"
            )
        )
    }

    // MainPage Navigator 구현체 생성
    val mainPageNavigator = remember {
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
        ScreenInfo("프로필 설정 화면", "dev_profile_setting"),
        ScreenInfo("지문 로그인 화면", "fingerprint_login")
    )

    NavHost(
        navController = navHostController,
        startDestination = "dev"
    ) {
        // 모드 선택 화면 (사용하지 않음, 개발자 모드에서 직접 사용자 모드로 이동 가능)
        composable("mode_selection") {
            ModeSelectionScreen(
                onUserModeClick = { mainPageNavigator.goToOnboarding(OnboardingRoute.SplashRoute) },
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
                        else -> navHostController.navigate(route) // 기타 route는 문자열로 처리
                    }
                },
                onUserModeClick = { mainPageNavigator.goToOnboarding(OnboardingRoute.SplashRoute) }
            )
        }

        // 개발자 모드용 화면들은 DevModeScreen에서 직접 MainPageRoute로 navigate하므로
        // 중계용 composable이 필요 없습니다.

        // 개발자 모드용 화면들 (기존 문자열 기반 route 유지)
        composable("dev_splash") {
            SplashScreen(
                onLoginClick = { mainPageNavigator.goToOnboarding(OnboardingRoute.LoginRoute) },
                onStartClick = { mainPageNavigator.goToOnboarding(OnboardingRoute.SignUpRoute) },
                onCheckClick = {}
            )
        }

        composable("dev_login") {
            LoginScreen(
                onBackClick = { navHostController.popBackStack() },
                onLoginClick = {},
                onSignUpClick = { mainPageNavigator.goToOnboarding(OnboardingRoute.SignUpRoute) },
                onFindIdClick = {}
            )
        }

        composable("dev_signup") {
            SignUpScreen(
                onBackClick = { navHostController.popBackStack() },
                onSettingClick = { mainPageNavigator.goToOnboarding(OnboardingRoute.ProfileSettingRoute) }
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
