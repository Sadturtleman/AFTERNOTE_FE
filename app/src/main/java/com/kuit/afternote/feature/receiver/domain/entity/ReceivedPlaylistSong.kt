package com.kuit.afternote.feature.receiver.domain.entity

/**
 * 수신한 애프터노트 플레이리스트의 노래 한 곡 (도메인 모델).
 *
 * GET /api/receiver-auth/after-notes/{afternoteId} 응답의 playlist.songs 항목에 대응합니다.
 */
data class ReceivedPlaylistSong(
    val title: String,
    val artist: String,
    val coverUrl: String? = null
)
