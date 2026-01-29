package com.kuit.afternote.app.navigation.navgraph

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kuit.afternote.feature.dailyrecord.presentation.navgraph.recordNavGraph
import com.kuit.afternote.feature.dailyrecord.presentation.navigiation.RecordRoute
import com.kuit.afternote.feature.dailyrecord.presentation.screen.RecordMainScreen
import com.kuit.afternote.feature.dev.presentation.screen.DevModeScreen
import com.kuit.afternote.feature.dev.presentation.screen.ModeSelectionScreen
import com.kuit.afternote.feature.dev.presentation.screen.ScreenInfo
import com.kuit.afternote.feature.mainpage.presentation.navgraph.MainPageRoute
import com.kuit.afternote.feature.mainpage.presentation.navgraph.mainPageNavGraph
import com.kuit.afternote.feature.mainpage.presentation.screen.AfternoteItemMapper
import com.kuit.afternote.feature.mainpage.presentation.screen.AfternoteMainRoute
import com.kuit.afternote.feature.mainpage.presentation.screen.MemorialPlaylistStateHolder
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.core.ui.screen.AfternoteDetailScreen
import com.kuit.afternote.core.ui.screen.rememberAfternoteDetailState
import com.kuit.afternote.feature.mainpage.presentation.screen.AfternoteEditScreen
import com.kuit.afternote.feature.mainpage.presentation.screen.FingerprintLoginScreen
import com.kuit.afternote.feature.onboarding.presentation.navgraph.OnboardingRoute
import com.kuit.afternote.feature.onboarding.presentation.navgraph.onboardingNavGraph
import com.kuit.afternote.feature.onboarding.presentation.screen.LoginScreen
import com.kuit.afternote.feature.onboarding.presentation.screen.ProfileSettingScreen
import com.kuit.afternote.feature.onboarding.presentation.screen.SignUpScreen
import com.kuit.afternote.feature.onboarding.presentation.screen.SplashScreen
import com.kuit.afternote.feature.setting.presentation.navgraph.SettingRoute
import com.kuit.afternote.feature.setting.presentation.navgraph.settingNavGraph
import androidx.activity.compose.BackHandler
import com.kuit.afternote.feature.receiver.presentation.screen.ReceiverAfterNoteMainScreen
import com.kuit.afternote.core.uimodel.AfternoteListDisplayItem
import com.kuit.afternote.feature.receiver.presentation.screen.ReceiverAfternoteListEvent
import com.kuit.afternote.feature.receiver.presentation.screen.ReceiverAfternoteListRoute
import com.kuit.afternote.feature.receiver.presentation.screen.ReceiverAfternoteListUiState
import com.kuit.afternote.R
import com.kuit.afternote.feature.timeletter.presentation.navgraph.TimeLetterRoute
import com.kuit.afternote.feature.timeletter.presentation.navgraph.timeLetterNavGraph
import com.kuit.afternote.ui.theme.AfternoteTheme

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Suppress("AssignedValueIsNeverRead")
fun NavGraph(navHostController: NavHostController) {
    var afternoteItems by remember { mutableStateOf(listOf<Pair<String, String>>()) }
    val playlistStateHolder = remember { MemorialPlaylistStateHolder() }
    val devModeScreens = listOf(
        ScreenInfo("메인 화면", "main"),
        ScreenInfo("메인 화면 (빈 상태)", "main_empty"),
        ScreenInfo("메인 화면 (목록 있음)", "main_with_items"),
        ScreenInfo("애프터노트 상세 화면", "afternote_detail"),
        ScreenInfo("애프터노트 수정 화면", "afternote_edit"),
        ScreenInfo("스플래시 화면", "dev_splash"),
        ScreenInfo("로그인 화면", "dev_login"),
        ScreenInfo("회원가입 화면", "dev_signup"),
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
            playlistStateHolder = playlistStateHolder
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
                        "main_empty" -> navHostController.navigate("main_empty")
                        "main_with_items" -> navHostController.navigate("main_with_items")
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

        // 메인 화면 - 빈 상태 (개발용)
        composable("main_empty") {
            AfternoteTheme(darkTheme = false) {
                AfternoteMainRoute(
                    onNavigateToDetail = { navHostController.navigate(MainPageRoute.DetailRoute) },
                    onNavigateToGalleryDetail = { navHostController.navigate(MainPageRoute.GalleryDetailRoute) },
                    onNavigateToAdd = { navHostController.navigate(MainPageRoute.EditRoute) },
                    initialItems = emptyList()
                )
            }
        }

        // 메인 화면 - 목록 있음 (개발용)
        composable("main_with_items") {
            AfternoteTheme(darkTheme = false) {
                AfternoteMainRoute(
                    onNavigateToDetail = { navHostController.navigate(MainPageRoute.DetailRoute) },
                    onNavigateToGalleryDetail = { navHostController.navigate(MainPageRoute.GalleryDetailRoute) },
                    onNavigateToAdd = { navHostController.navigate(MainPageRoute.EditRoute) },
                    initialItems = AfternoteItemMapper.toAfternoteItems(
                        listOf(
                            "갤러리" to "2025.01.28",
                            "추모 가이드라인" to "2025.01.28"
                        )
                    )
                )
            }
        }

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
                onBackClick = { navHostController.popBackStack() },
                onAddProfileAvatarClick = {}
            )
        }

        // 수신자 애프터노트 메인 (개발자 모드용)
        composable("receiver_afternote_main") {
            ReceiverAfterNoteMainScreen(
                title = "박서연",
                onNavigateToFullList = { navHostController.navigate("receiver_afternote_list") }
            )
        }

        // 수신자 애프터노트 전체 목록 (애프터노트 전체 확인하기 진입)
        composable("receiver_afternote_list") {
            BackHandler { navHostController.popBackStack() }
            var listState by remember {
                mutableStateOf(
                    ReceiverAfternoteListUiState(
                        items = listOf(
                            AfternoteListDisplayItem(
                                id = "1",
                                serviceName = "추모 가이드라인",
                                date = "2025.12.01",
                                iconResId = R.drawable.img_logo
                            ),
                            AfternoteListDisplayItem(
                                id = "2",
                                serviceName = "갤러리",
                                date = "2025.12.02",
                                iconResId = R.drawable.img_insta_pattern
                            ),
                            AfternoteListDisplayItem(
                                id = "3",
                                serviceName = "인스타그램",
                                date = "2025.12.03",
                                iconResId = R.drawable.img_insta_pattern
                            )
                        )
                    )
                )
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
        composable("receiver_afternote_detail/{itemId}") {
            // TODO: itemId = backStackEntry.arguments?.getString("itemId") 로 데이터 로드 구현
            AfternoteTheme(darkTheme = false) {
                AfternoteDetailScreen(
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
