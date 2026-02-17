package com.kuit.afternote.feature.receiver.domain.entity

/**
 * 수신자에게 공유된 마인드레코드 한 건을 나타내는 도메인 엔티티.
 *
 * GET /api/received/{receiverId}/mind-records 응답의 한 항목에 대응합니다.
 */
data class ReceivedMindRecord(
    val mindRecordId: Long,
    val sourceType: String?,
    val content: String?,
    val recordDate: String?
)
