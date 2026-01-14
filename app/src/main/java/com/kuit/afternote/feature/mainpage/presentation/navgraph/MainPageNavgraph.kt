package com.kuit.afternote.feature.mainpage.presentation.navgraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kuit.afternote.core.navigation.MainPageNavigator
import com.kuit.afternote.feature.mainpage.presentation.screen.AfternoteDetailScreen
import com.kuit.afternote.feature.mainpage.presentation.screen.AfternoteEditScreen
import com.kuit.afternote.feature.mainpage.presentation.screen.AfternoteMainScreen
import com.kuit.afternote.feature.mainpage.presentation.screen.FingerprintLoginScreen
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
        AfternoteMainScreen(
            afternoteItems = emptyList(),
            onItemClick = { navController.navigate(MainPageRoute.DetailRoute) },
            onAddClick = { navController.navigate(MainPageRoute.EditRoute) }
        )
    }

    mainPageComposable<MainPageRoute.MainWithItemsRoute> {
        AfternoteMainScreen(
            afternoteItems = afternoteItems,
            onItemClick = { navController.navigate(MainPageRoute.DetailRoute) },
            onAddClick = { navController.navigate(MainPageRoute.EditRoute) }
        )
    }

    mainPageComposable<MainPageRoute.DetailRoute> {
        AfternoteDetailScreen(
            onBackClick = { navController.popBackStack() },
            onEditClick = { navController.navigate(MainPageRoute.EditRoute) }
        )
    }

    mainPageComposable<MainPageRoute.EditRoute> {
        AfternoteEditScreen(
            onBackClick = { navController.popBackStack() },
            onRegisterClick = { selectedService ->
                // 현재 날짜를 포맷팅
                val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
                val currentDate = dateFormat.format(Date())

                // 목록에 추가
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
            }
        )
    }

    mainPageComposable<MainPageRoute.FingerprintLoginRoute> {
        FingerprintLoginScreen(
            onFingerprintAuthClick = { /* TODO: 지문 인증 처리 */ }
        )
    }
}
