package com.kuit.afternote.feature.receiver.domain.entity

/**
 * 수신한 애프터노트의 플레이리스트 정보 (도메인 모델).
 *
 * GET /api/receiver-auth/after-notes/{afternoteId} 응답의 playlist에 대응합니다.
 */
data class ReceivedAfternotePlaylist(
    val atmosphere: String? = null,
    val songs: List<ReceivedPlaylistSong> = emptyList()
)
