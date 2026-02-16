package com.kuit.afternote.feature.receiver.domain.entity

/**
 * 수신 타임레터에 첨부된 미디어 한 건.
 *
 * GET /api/received/{receiverId}/time-letters 응답의 mediaList 항목에 대응합니다.
 */
data class ReceivedTimeLetterMedia(
    val id: Long,
    val mediaType: String?,
    val mediaUrl: String
)
