package com.kuit.afternote.feature.mainpage.presentation.navgraph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kuit.afternote.core.navigation.MainPageNavigator
import com.kuit.afternote.feature.mainpage.presentation.screen.AfternoteDetailScreen
import com.kuit.afternote.feature.mainpage.presentation.screen.AfternoteEditScreen
import com.kuit.afternote.feature.mainpage.presentation.screen.AfternoteItemMapper
import com.kuit.afternote.feature.mainpage.presentation.screen.AfternoteMainRoute
import com.kuit.afternote.feature.mainpage.presentation.screen.FingerprintLoginScreen
import com.kuit.afternote.feature.mainpage.presentation.screen.AddSongCallbacks
import com.kuit.afternote.feature.mainpage.presentation.screen.AddSongScreen
import com.kuit.afternote.feature.mainpage.presentation.screen.GalleryDetailCallbacks
import com.kuit.afternote.feature.mainpage.presentation.screen.GalleryDetailScreen
import com.kuit.afternote.feature.mainpage.presentation.screen.GalleryDetailState
import com.kuit.afternote.feature.mainpage.presentation.screen.MemorialPlaylistCallbacks
import com.kuit.afternote.feature.mainpage.presentation.screen.MemorialPlaylistScreen
import com.kuit.afternote.feature.mainpage.presentation.screen.rememberMemorialPlaylistStateHolder
import com.kuit.afternote.ui.theme.AfternoteTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * mainpage feature 전용 라이트 모드 테마 래퍼
 * Preview 함수에서 사용하여 다크 모드를 강제로 비활성화합니다.
 * 
 * 사용 예시:
 * ```
 * @Preview(showBackground = true)
 * @Composable
 * private fun MyScreenPreview() {
 *     MainPageLightTheme {
 *         MyScreen()
 *     }
 * }
 * ```
 */
@Composable
fun MainPageLightTheme(content: @Composable () -> Unit) {
    AfternoteTheme(darkTheme = false, content = content)
}

/**
 * mainpage feature 전용 composable 래퍼
 * 내부적으로 라이트 모드를 강제 적용하여 다크모드를 비활성화합니다.
 */
inline fun <reified T : Any> NavGraphBuilder.mainPageComposable(noinline content: @Composable (NavBackStackEntry) -> Unit) {
    composable<T> { backStackEntry ->
        AfternoteTheme(darkTheme = false) {
            content(backStackEntry)
        }
    }
}

fun NavGraphBuilder.mainPageNavGraph(
    navController: NavController,
    afternoteItems: List<Pair<String, String>>,
    onItemsUpdated: (List<Pair<String, String>>) -> Unit,
    mainPageNavigator: MainPageNavigator
) {
    mainPageComposable<MainPageRoute.MainEmptyRoute> {
        AfternoteMainRoute(
            onNavigateToDetail = { navController.navigate(MainPageRoute.DetailRoute) },
            onNavigateToGalleryDetail = { navController.navigate(MainPageRoute.GalleryDetailRoute) },
            onNavigateToAdd = { navController.navigate(MainPageRoute.EditRoute) },
            initialItems = emptyList()
        )
    }

    mainPageComposable<MainPageRoute.MainWithItemsRoute> {
        AfternoteMainRoute(
            onNavigateToDetail = { navController.navigate(MainPageRoute.DetailRoute) },
            onNavigateToGalleryDetail = { navController.navigate(MainPageRoute.GalleryDetailRoute) },
            onNavigateToAdd = { navController.navigate(MainPageRoute.EditRoute) },
            initialItems = AfternoteItemMapper.toAfternoteItems(afternoteItems)
        )
    }

    mainPageComposable<MainPageRoute.DetailRoute> {
        AfternoteDetailScreen(
            onBackClick = { navController.popBackStack() },
            onEditClick = { navController.navigate(MainPageRoute.EditRoute) }
        )
    }

    mainPageComposable<MainPageRoute.GalleryDetailRoute> {
        GalleryDetailScreen(
            detailState = GalleryDetailState(),
            callbacks = GalleryDetailCallbacks(
                onBackClick = { navController.popBackStack() },
                onEditClick = { navController.navigate(MainPageRoute.EditRoute) }
            )
        )
    }

    mainPageComposable<MainPageRoute.EditRoute> {
        // 공유 플레이리스트 상태 생성 (EditRoute와 MemorialPlaylistRoute 간 공유)
        val playlistStateHolder = remember { 
            com.kuit.afternote.feature.mainpage.presentation.screen.MemorialPlaylistStateHolder().apply {
                // 초기 데이터 설정
                if (songs.isEmpty()) {
                    initializeSongs(
                        listOf(
                            com.kuit.afternote.feature.mainpage.presentation.component.edit.model.Song(id = "1", title = "노래 제목", artist = "가수 이름"),
                            com.kuit.afternote.feature.mainpage.presentation.component.edit.model.Song(id = "2", title = "노래 제목", artist = "가수 이름"),
                            com.kuit.afternote.feature.mainpage.presentation.component.edit.model.Song(id = "3", title = "노래 제목", artist = "가수 이름"),
                            com.kuit.afternote.feature.mainpage.presentation.component.edit.model.Song(id = "4", title = "노래 제목", artist = "가수 이름"),
                            com.kuit.afternote.feature.mainpage.presentation.component.edit.model.Song(id = "5", title = "노래 제목", artist = "가수 이름"),
                            com.kuit.afternote.feature.mainpage.presentation.component.edit.model.Song(id = "6", title = "노래 제목", artist = "가수 이름"),
                            com.kuit.afternote.feature.mainpage.presentation.component.edit.model.Song(id = "7", title = "노래 제목", artist = "가수 이름"),
                            com.kuit.afternote.feature.mainpage.presentation.component.edit.model.Song(id = "8", title = "노래 제목", artist = "가수 이름"),
                            com.kuit.afternote.feature.mainpage.presentation.component.edit.model.Song(id = "9", title = "노래 제목", artist = "가수 이름"),
                            com.kuit.afternote.feature.mainpage.presentation.component.edit.model.Song(id = "10", title = "노래 제목", artist = "가수 이름"),
                            com.kuit.afternote.feature.mainpage.presentation.component.edit.model.Song(id = "11", title = "노래 제목", artist = "가수 이름")
                        )
                    )
                }
                // AfternoteEditState와 동기화를 위한 콜백 설정
                onSongCountChanged = {
                    // 콜백은 AfternoteEditScreen에서 설정됨
                }
            }
        }
        
        AfternoteEditScreen(
            onBackClick = { navController.popBackStack() },
            onRegisterClick = { selectedService ->
                // 현재 날짜를 포맷팅
                val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
                val currentDate = dateFormat.format(Date())

                // 목록에 추가 (레거시 형식 유지)
                val newItem = selectedService to currentDate
                val updatedItems = afternoteItems + newItem
                onItemsUpdated(updatedItems)

                // 메인 화면으로 이동
                navController.navigate(MainPageRoute.MainWithItemsRoute) {
                    // 백 스택에서 MainEmptyRoute까지 제거 (시작점으로 돌아감)
                    popUpTo(MainPageRoute.MainEmptyRoute) { inclusive = true }
                    // 같은 Route가 이미 있으면 재사용
                    launchSingleTop = true
                }
            },
            onNavigateToPlaylist = { navController.navigate(MainPageRoute.MemorialPlaylistRoute) },
            onNavigateToAddSong = { navController.navigate(MainPageRoute.MemorialPlaylistRoute) },
            playlistStateHolder = playlistStateHolder
        )
    }

    mainPageComposable<MainPageRoute.FingerprintLoginRoute> {
        FingerprintLoginScreen(
            onFingerprintAuthClick = { /* TODO: 지문 인증 처리 */ }
        )
    }

    mainPageComposable<MainPageRoute.MemorialPlaylistRoute> {
        // EditRoute에서 생성한 공유 플레이리스트 상태 재사용
        // 주의: 실제로는 NavBackStackEntry의 savedStateHandle을 사용하거나
        // ViewModel을 통해 상태를 공유해야 하지만, 현재는 간단한 해결책으로
        // rememberMemorialPlaylistStateHolder를 사용 (실제로는 공유되지 않음)
        // TODO: 실제 상태 공유를 위해 ViewModel 또는 savedStateHandle 사용 고려
        val playlistStateHolder = rememberMemorialPlaylistStateHolder()
        
        MemorialPlaylistScreen(
            stateHolder = playlistStateHolder,
            callbacks = MemorialPlaylistCallbacks(
                onBackClick = { navController.popBackStack() },
                onAddSongClick = { navController.navigate(MainPageRoute.AddSongRoute) }
            )
        )
    }

    mainPageComposable<MainPageRoute.AddSongRoute> {
        AddSongScreen(
            selectedSongIds = emptySet(),
            callbacks = AddSongCallbacks(
                onBackClick = { navController.popBackStack() },
                onSongSelected = { song ->
                    // TODO: 선택된 노래를 플레이리스트에 추가
                    navController.popBackStack()
                }
            )
        )
    }
}
