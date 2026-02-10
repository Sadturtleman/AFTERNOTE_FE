package com.kuit.afternote.feature.receiver.domain.entity

/**
 * 수신자에게 전달된 애프터노트 한 건을 나타내는 도메인 엔티티.
 *
 * GET /api/received/{receiverId}/after-notes 응답의 한 항목에 대응합니다.
 */
data class ReceivedAfternote(
    val sourceType: String?,
    val lastUpdatedAt: String?
)
