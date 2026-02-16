    package com.kuit.afternote.feature.receiver.presentation.navgraph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.kuit.afternote.feature.receiver.presentation.screen.timeletter.TimeLetterScreen
import com.kuit.afternote.feature.receiver.presentation.viewmodel.ReceiverTimeLetterViewModel

private const val ROUTE_DETAIL = "receiver_time_letter_detail"

/**
 * 수신자 타임레터 목록 라우트. ViewModel을 주입하고 화면에 상태를 전달합니다.
 */
@Composable
fun ReceiverTimeLetterRoute(
    navController: NavHostController,
    onBackClick: () -> Unit,
    viewModel: ReceiverTimeLetterViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val receiverId = navController.currentBackStackEntry?.arguments?.getString("receiverId") ?: ""
    TimeLetterScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onLetterClick = { letter ->
            navController.navigate("$ROUTE_DETAIL/$receiverId/${letter.timeLetterReceiverId}")
        },
        onBottomNavSelected = viewModel::updateSelectedBottomNavItem,
        onSortByDate = { viewModel.sortByDateAscending() },
        onSortByUnread = { viewModel.sortByUnreadFirst() }
    )
}
