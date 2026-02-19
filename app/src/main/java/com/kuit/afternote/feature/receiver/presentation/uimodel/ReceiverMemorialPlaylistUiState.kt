package com.kuit.afternote.feature.receiver.presentation.uimodel

import com.kuit.afternote.core.uimodel.PlaylistSongDisplay

/**
 * 수신자 추모 플레이리스트 화면 UI 상태.
 *
 * GET /api/receiver-auth/after-notes/{afternoteId}의 playlist.songs 및
 * playlist.memorialVideo(장례식에 남길 영상)를 표시합니다.
 */
data class ReceiverMemorialPlaylistUiState(
    val songs: List<PlaylistSongDisplay> = emptyList(),
    val memorialVideoUrl: String? = null,
    val memorialThumbnailUrl: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
