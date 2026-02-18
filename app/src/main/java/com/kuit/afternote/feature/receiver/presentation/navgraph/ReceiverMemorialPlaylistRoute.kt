package com.kuit.afternote.feature.receiver.presentation.navgraph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuit.afternote.feature.receiver.presentation.screen.afternote.MemorialPlaylistScreen
import com.kuit.afternote.feature.receiver.presentation.viewmodel.ReceiverMemorialPlaylistViewModel

/**
 * 수신자 추모 플레이리스트 라우트.
 * GET /api/receiver-auth/after-notes/{afternoteId}의 playlist.songs를 표시합니다.
 */
@Composable
fun ReceiverMemorialPlaylistRoute(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    viewModel: ReceiverMemorialPlaylistViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    MemorialPlaylistScreen(
        modifier = modifier,
        songs = uiState.songs,
        onBackClick = onBackClick
    )
}
