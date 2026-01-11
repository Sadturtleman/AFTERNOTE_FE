package com.kuit.afternote.feature.mainpage.presentation.navgraph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kuit.afternote.core.navigation.MainPageNavigator
import com.kuit.afternote.feature.mainpage.presentation.screen.AfternoteDetailScreen
import com.kuit.afternote.feature.mainpage.presentation.screen.AfternoteEditScreen
import com.kuit.afternote.feature.mainpage.presentation.screen.AfternoteMainScreen
import com.kuit.afternote.feature.mainpage.presentation.screen.FingerprintLoginScreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun NavGraphBuilder.mainPageNavGraph(
    navController: NavController,
    afternoteItems: List<Pair<String, String>>,
    onItemsUpdated: (List<Pair<String, String>>) -> Unit,
    mainPageNavigator: MainPageNavigator
) {
    composable<MainPageRoute.MainEmptyRoute> {
        AfternoteMainScreen(
            afternoteItems = emptyList(),
            onItemClick = { navController.navigate(MainPageRoute.DetailRoute) },
            onAddClick = { navController.navigate(MainPageRoute.EditRoute) }
        )
    }

    composable<MainPageRoute.MainWithItemsRoute> {
        AfternoteMainScreen(
            afternoteItems = afternoteItems,
            onItemClick = { navController.navigate(MainPageRoute.DetailRoute) },
            onAddClick = { navController.navigate(MainPageRoute.EditRoute) }
        )
    }

    composable<MainPageRoute.DetailRoute> {
        AfternoteDetailScreen(
            onBackClick = { navController.popBackStack() },
            onEditClick = { navController.navigate(MainPageRoute.EditRoute) }
        )
    }

    composable<MainPageRoute.EditRoute> {
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

    composable<MainPageRoute.FingerprintLoginRoute> {
        FingerprintLoginScreen(
            onFingerprintAuthClick = { /* TODO: 지문 인증 처리 */ }
        )
    }
}
