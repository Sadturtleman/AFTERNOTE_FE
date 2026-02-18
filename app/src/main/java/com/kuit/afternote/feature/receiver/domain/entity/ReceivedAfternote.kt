package com.kuit.afternote.feature.receiver.domain.entity

/**
 * 수신자에게 전달된 애프터노트 한 건을 나타내는 도메인 엔티티.
 *
 * GET /api/receiver-auth/after-notes 응답의 한 항목에 대응합니다.
 *
 * @param id 애프터노트 ID (상세 조회·플레이리스트 라우트 등에서 사용)
 */
data class ReceivedAfternote(
    val id: Long,
    val sourceType: String?,
    val lastUpdatedAt: String?,
    val leaveMessage: String? = null
)
