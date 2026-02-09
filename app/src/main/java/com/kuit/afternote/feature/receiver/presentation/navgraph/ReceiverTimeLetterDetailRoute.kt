package com.kuit.afternote.feature.receiver.presentation.navgraph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuit.afternote.feature.receiver.presentation.screen.TimeLetterDetailScreen
import com.kuit.afternote.feature.receiver.presentation.viewmodel.ReceiverTimeLetterDetailViewModel

/**
 * 수신자 타임레터 상세 라우트. ViewModel을 주입하고 화면에 상태를 전달합니다.
 */
@Composable
fun ReceiverTimeLetterDetailRoute(
    onBackClick: () -> Unit,
    viewModel: ReceiverTimeLetterDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    TimeLetterDetailScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onBottomNavSelected = viewModel::updateSelectedBottomNavItem
    )
}
