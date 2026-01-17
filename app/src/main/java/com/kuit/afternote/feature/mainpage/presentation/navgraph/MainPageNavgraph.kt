package com.kuit.afternote.feature.mainpage.presentation.navgraph

import androidx.compose.runtime.Composable
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
import com.kuit.afternote.ui.theme.AfternoteTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
            onNavigateToAddSong = { navController.navigate(MainPageRoute.MemorialPlaylistRoute) }
        )
    }

    mainPageComposable<MainPageRoute.FingerprintLoginRoute> {
        FingerprintLoginScreen(
            onFingerprintAuthClick = { /* TODO: 지문 인증 처리 */ }
        )
    }

    mainPageComposable<MainPageRoute.MemorialPlaylistRoute> {
        MemorialPlaylistScreen(
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
