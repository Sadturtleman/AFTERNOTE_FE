package com.kuit.afternote.feature.receiver.domain.entity

/**
 * 수신한 애프터노트 상세 (도메인 모델).
 *
 * GET /api/receiver-auth/after-notes/{afternoteId} 응답에 대응합니다.
 * 플레이리스트 화면에서는 [playlist]의 [ReceivedAfternotePlaylist.songs]를 사용합니다.
 */
data class ReceivedAfternoteDetail(
    val id: Long,
    val category: String? = null,
    val title: String? = null,
    val playlist: ReceivedAfternotePlaylist? = null
)
