package com.kuit.afternote.app.navigation.navgraph

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kuit.afternote.app.compositionlocal.DataProviderLocals
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.core.ui.screen.AfternoteDetailScreen
import com.kuit.afternote.core.ui.screen.rememberAfternoteDetailState
import com.kuit.afternote.core.uimodel.AfternoteListDisplayItem
import com.kuit.afternote.feature.dailyrecord.presentation.navgraph.recordNavGraph
import com.kuit.afternote.feature.dailyrecord.presentation.navigiation.RecordRoute
import com.kuit.afternote.feature.dailyrecord.presentation.screen.RecordMainScreen
import com.kuit.afternote.feature.dev.presentation.screen.DevModeScreen
import com.kuit.afternote.feature.dev.presentation.screen.ModeSelectionScreen
import com.kuit.afternote.feature.dev.presentation.screen.ScreenInfo
import com.kuit.afternote.feature.mainpage.presentation.navgraph.MainPageRoute
import com.kuit.afternote.feature.mainpage.presentation.navgraph.mainPageNavGraph
import com.kuit.afternote.feature.mainpage.presentation.screen.FingerprintLoginScreen
import com.kuit.afternote.feature.mainpage.presentation.screen.MemorialPlaylistStateHolder
import com.kuit.afternote.feature.onboarding.presentation.navgraph.OnboardingRoute
import com.kuit.afternote.feature.onboarding.presentation.navgraph.onboardingNavGraph
import com.kuit.afternote.feature.onboarding.presentation.screen.LoginScreen
import com.kuit.afternote.feature.onboarding.presentation.screen.ProfileSettingScreen
import com.kuit.afternote.feature.onboarding.presentation.screen.SignUpScreen
import com.kuit.afternote.feature.onboarding.presentation.screen.SplashScreen
import com.kuit.afternote.feature.receiver.presentation.screen.ReceiverAfterNoteMainScreen
import com.kuit.afternote.feature.receiver.presentation.screen.ReceiverAfternoteListEvent
import com.kuit.afternote.feature.receiver.presentation.screen.ReceiverAfternoteListRoute
import com.kuit.afternote.feature.receiver.presentation.screen.ReceiverAfternoteListUiState
import com.kuit.afternote.feature.setting.presentation.navgraph.SettingRoute
import com.kuit.afternote.feature.setting.presentation.navgraph.settingNavGraph
import com.kuit.afternote.feature.timeletter.presentation.navgraph.TimeLetterRoute
import com.kuit.afternote.feature.timeletter.presentation.navgraph.timeLetterNavGraph
import com.kuit.afternote.ui.theme.AfternoteTheme

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Suppress("AssignedValueIsNeverRead")
fun NavGraph(navHostController: NavHostController) {
    val afternoteProvider = DataProviderLocals.LocalAfternoteEditDataProvider.current
    val receiverProvider = DataProviderLocals.LocalReceiverDataProvider.current
    var afternoteItems by remember { mutableStateOf(listOf<Pair<String, String>>()) }
    val playlistStateHolder = remember { MemorialPlaylistStateHolder() }
    val devModeScreens = listOf(
        ScreenInfo("메인 화면", "main"),
        ScreenInfo("애프터노트 상세 화면", "afternote_detail"),
        ScreenInfo("애프터노트 수정 화면", "afternote_edit"),
        ScreenInfo("스플래시 화면", "dev_splash"),
        ScreenInfo("로그인 화면", "dev_login"),
        ScreenInfo("마음의기록 메인 화면", "record_main"),
        ScreenInfo("지문 로그인 화면", "fingerprint_login"),
        ScreenInfo("타임레터 화면", "time_letter_main"),
        ScreenInfo("타임레터 작성 화면", "time_letter_writer"),
        ScreenInfo("임시저장 화면", "draft_letter"),
        ScreenInfo("수신자 목록 화면", "receive_list"),
        ScreenInfo("수신자 애프터노트 메인", "receiver_afternote_main"),
        ScreenInfo("타임레터 빈 화면", "letter_empty"),
        ScreenInfo("설정 화면", "setting_main")
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

        recordNavGraph(navHostController)

        mainPageNavGraph(
            navController = navHostController,
            afternoteItems = afternoteItems,
            onItemsUpdated = { afternoteItems = it },
            playlistStateHolder = playlistStateHolder,
            afternoteProvider = afternoteProvider
        )

        timeLetterNavGraph(
            navController = navHostController
        )

        settingNavGraph(
            navController = navHostController
        )

        // 개발자 모드 화면 (기본 시작 화면)
        composable("dev") {
            DevModeScreen(
                screens = devModeScreens,
                onScreenClick = { route ->

                    // 문자열 route를 MainPageRoute로 변환하여 navigate
                    when (route) {
                        "main" -> navHostController.navigate(MainPageRoute.MainRoute)
                        "afternote_detail" -> navHostController.navigate(MainPageRoute.DetailRoute)
                        "afternote_edit" -> navHostController.navigate(MainPageRoute.EditRoute)
                        "fingerprint_login" -> navHostController.navigate(MainPageRoute.FingerprintLoginRoute)
                        "time_letter_main" -> navHostController.navigate(TimeLetterRoute.TimeLetterMainRoute)
                        "time_letter_writer" -> navHostController.navigate(TimeLetterRoute.TimeLetterWriterRoute)
                        "draft_letter" -> navHostController.navigate(TimeLetterRoute.DraftLetterRoute)
                        "receive_list" -> navHostController.navigate(TimeLetterRoute.ReceiveListRoute)
                        "receiver_afternote_main" -> navHostController.navigate("receiver_afternote_main")
                        "letter_empty" -> navHostController.navigate(TimeLetterRoute.LetterEmptyRoute)
                        "setting_main" -> navHostController.navigate(SettingRoute.SettingMainRoute)
                        else -> navHostController.navigate(route) // 기타 route는 문자열로 처리
                    }
                },
                onUserModeClick = { navHostController.navigate(OnboardingRoute.SplashRoute) }
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
                onLoginSuccess = { navHostController.navigate(MainPageRoute.MainRoute) }
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
                onFinishClick = { navHostController.navigate(MainPageRoute.MainRoute) },
                onBackClick = { navHostController.popBackStack() }
            )
        }

        // 수신자 애프터노트 메인 (개발자 모드용)
        composable("receiver_afternote_main") {
            ReceiverAfterNoteMainScreen(
                title = receiverProvider.getDefaultReceiverTitleForDev(),
                albumCovers = afternoteProvider.getAlbumCovers(),
                onNavigateToFullList = { navHostController.navigate("receiver_afternote_list") },
                onBackClick = { navHostController.popBackStack() }
            )
        }

        // 수신자 애프터노트 전체 목록 (애프터노트 전체 확인하기 진입)
        composable("receiver_afternote_list") {
            BackHandler { navHostController.popBackStack() }
            val afternoteItems = remember(receiverProvider) {
                receiverProvider.getAfternoteListSeedsForReceiverList().map { seed ->
                    AfternoteListDisplayItem(
                        id = seed.id,
                        serviceName = seed.serviceNameLiteral ?: "",
                        date = seed.date,
                        iconResId = seed.iconResId
                    )
                }
            }
            var listState by remember {
                mutableStateOf(ReceiverAfternoteListUiState(items = afternoteItems))
            }
            ReceiverAfternoteListRoute(
                uiState = listState,
                onEvent = { event ->
                    listState = when (event) {
                        is ReceiverAfternoteListEvent.SelectTab ->
                            listState.copy(selectedTab = event.tab)
                        is ReceiverAfternoteListEvent.SelectBottomNav ->
                            listState.copy(selectedBottomNavItem = event.navItem)
                        is ReceiverAfternoteListEvent.ClickItem -> {
                            navHostController.navigate("receiver_afternote_detail/${event.itemId}")
                            listState
                        }
                    }
                }
            )
        }

        // 수신자 애프터노트 상세 (읽기 전용)
        composable("receiver_afternote_detail/{itemId}") { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId")
            val seed =
                remember(receiverProvider, itemId) {
                    receiverProvider
                        .getAfternoteListSeedsForReceiverList()
                        .firstOrNull { it.id == itemId }
                        ?: receiverProvider.getAfternoteListSeedsForReceiverList().firstOrNull()
                }
            val serviceName = seed?.serviceNameLiteral ?: ""
            val userName = receiverProvider.getDefaultReceiverTitleForDev()

            AfternoteTheme(darkTheme = false) {
                AfternoteDetailScreen(
                    serviceName = serviceName,
                    userName = userName,
                    isEditable = false,
                    onBackClick = { navHostController.popBackStack() },
                    state = rememberAfternoteDetailState(
                        defaultBottomNavItem = BottomNavItem.AFTERNOTE
                    )
                )
            }
        }

        // 지문 로그인 화면
        composable("fingerprint_login") {
            FingerprintLoginScreen(
                onFingerprintAuthClick = { /* TODO: 지문 인증 처리 */ }
            )
        }
        composable("record_main") {
            RecordMainScreen(
                onDiaryClick = { navHostController.navigate(RecordRoute.ListRoute) },
                onDeepMindClick = { navHostController.navigate(RecordRoute.ListRoute) },
                onWeekendReportClick = { navHostController.navigate(RecordRoute.WeekendReportRoute) },
                onQuestionClick = { navHostController.navigate(RecordRoute.QuestionRouteList) }
            )
        }
    }
}
