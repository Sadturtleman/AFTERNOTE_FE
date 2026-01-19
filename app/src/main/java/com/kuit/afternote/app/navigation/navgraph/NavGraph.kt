package com.kuit.afternote.app.navigation.navgraph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kuit.afternote.feature.dev.presentation.screen.DevModeScreen
import com.kuit.afternote.feature.dev.presentation.screen.ModeSelectionScreen
import com.kuit.afternote.feature.dev.presentation.screen.ScreenInfo
import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.Song
import com.kuit.afternote.feature.mainpage.presentation.screen.AddSongCallbacks
import com.kuit.afternote.feature.mainpage.presentation.screen.AddSongScreen
import com.kuit.afternote.feature.mainpage.presentation.screen.AfternoteDetailScreen
import com.kuit.afternote.feature.mainpage.presentation.screen.AfternoteEditScreen
import com.kuit.afternote.feature.mainpage.presentation.screen.AfternoteItemMapper
import com.kuit.afternote.feature.mainpage.presentation.screen.AfternoteMainRoute
import com.kuit.afternote.feature.mainpage.presentation.screen.FingerprintLoginScreen
import com.kuit.afternote.feature.mainpage.presentation.screen.MemorialPlaylistRouteScreen
import com.kuit.afternote.feature.mainpage.presentation.screen.MemorialPlaylistStateHolder
import com.kuit.afternote.feature.mainpage.presentation.screen.rememberAfternoteEditState
import com.kuit.afternote.feature.onboarding.presentation.navgraph.OnboardingRoute
import com.kuit.afternote.feature.onboarding.presentation.navgraph.onboardingNavGraph
import com.kuit.afternote.feature.onboarding.presentation.screen.LoginScreen
import com.kuit.afternote.feature.onboarding.presentation.screen.ProfileSettingScreen
import com.kuit.afternote.feature.onboarding.presentation.screen.SignUpScreen
import com.kuit.afternote.feature.onboarding.presentation.screen.SplashScreen
import com.kuit.afternote.ui.theme.AfternoteTheme

@Composable
fun NavGraph(navHostController: NavHostController) {
    val playlistStateHolder = remember { MemorialPlaylistStateHolder() }
    val editState = rememberAfternoteEditState()

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
                onUserModeClick = { navHostController.navigate(OnboardingRoute.SplashRoute) },
                onDevModeClick = { navHostController.navigate("dev") }
            )
        }

        onboardingNavGraph(navHostController)

        // 개발자 모드 화면 (기본 시작 화면)
        composable("dev") {
            DevModeScreen(
                screens = devModeScreens,
                onScreenClick = { route -> navHostController.navigate(route) },
                onUserModeClick = { navHostController.navigate(OnboardingRoute.SplashRoute) }
            )
        }

        // 메인 화면 - 빈 상태 (개발용)
        composable("main_empty") {
            AfternoteTheme(darkTheme = false) {
                AfternoteMainRoute(
                    onNavigateToDetail = { navHostController.navigate("afternote_detail") },
                    onNavigateToGalleryDetail = { navHostController.navigate("afternote_detail") },
                    onNavigateToAdd = { navHostController.navigate("afternote_edit") },
                    initialItems = emptyList()
                )
            }
        }

        // 메인 화면 - 목록 있음 (개발용)
        composable("main_with_items") {
            AfternoteTheme(darkTheme = false) {
                AfternoteMainRoute(
                    onNavigateToDetail = { navHostController.navigate("afternote_detail") },
                    onNavigateToGalleryDetail = { navHostController.navigate("afternote_detail") },
                    onNavigateToAdd = { navHostController.navigate("afternote_edit") },
                    initialItems = AfternoteItemMapper.toAfternoteItems(
                        listOf(
                            "인스타그램" to "2023.11.24",
                            "갤러리" to "2023.11.25",
                            "갤러리" to "2023.11.26",
                            "인스타그램" to "2023.11.27"
                        )
                    )
                )
            }
        }

        // 애프터노트 상세 화면
        composable("afternote_detail") {
            AfternoteTheme(darkTheme = false) {
                AfternoteDetailScreen(
                    onBackClick = { navHostController.popBackStack() },
                    onEditClick = { navHostController.navigate("afternote_edit") }
                )
            }
        }

        // 애프터노트 수정 화면
        composable("afternote_edit") {
            LaunchedEffect(Unit) {
                if (playlistStateHolder.songs.isEmpty()) {
                    playlistStateHolder.initializeSongs(
                        (1..11).map {
                            Song(id = "$it", title = "노래 제목", artist = "가수 이름")
                        }
                    )
                }
            }
            AfternoteTheme(darkTheme = false) {
                AfternoteEditScreen(
                    onBackClick = { navHostController.popBackStack() },
                    onRegisterClick = { /* TODO: 등록 처리 */ },
                    onNavigateToAddSong = { navHostController.navigate("memorial_playlist") },
                    state = editState,
                    playlistStateHolder = playlistStateHolder
                )
            }
        }

        // 추모 플레이리스트 (헤더 "추모 플레이리스트", 노래 추가하기 → add_song)
        composable("memorial_playlist") {
            AfternoteTheme(darkTheme = false) {
                MemorialPlaylistRouteScreen(
                    playlistStateHolder = playlistStateHolder,
                    onBackClick = { navHostController.popBackStack() },
                    onNavigateToAddSong = { navHostController.navigate("add_song") }
                )
            }
        }

        // 추모 플레이리스트 추가 (헤더 "추모 플레이리스트 추가", 검색+선택 후 담기)
        composable("add_song") {
            AfternoteTheme(darkTheme = false) {
                AddSongScreen(
                    callbacks = AddSongCallbacks(
                        onBackClick = { navHostController.popBackStack() },
                        onSongsAdded = { songs: List<Song> ->
                            songs.forEach { playlistStateHolder.addSong(it) }
                            navHostController.popBackStack()
                        }
                    )
                )
            }
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
            AfternoteTheme(darkTheme = false) {
                FingerprintLoginScreen(
                    onFingerprintAuthClick = { /* TODO: 지문 인증 처리 */ }
                )
            }
        }
    }
}
