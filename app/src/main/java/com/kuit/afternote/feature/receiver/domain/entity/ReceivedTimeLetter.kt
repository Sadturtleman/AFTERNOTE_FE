package com.kuit.afternote.feature.receiver.domain.entity

/**
 * 수신자에게 배달된 타임레터 한 건을 나타내는 도메인 엔티티.
 *
 * GET /api/received/{receiverId}/time-letters 응답의 한 항목에 대응합니다.
 */
data class ReceivedTimeLetter(
    val timeLetterId: Long,
    val receiverName: String?,
    val sendAt: String?,
    val title: String?,
    val content: String?
)
