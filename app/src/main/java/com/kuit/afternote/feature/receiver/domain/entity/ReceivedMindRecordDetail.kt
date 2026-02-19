package com.kuit.afternote.feature.receiver.domain.entity

/**
 * 수신한 마인드레코드 상세 한 건.
 *
 * GET /api/receiver-auth/mind-records/{mindRecordId} 응답에 대응합니다.
 *
 * @param imageUrls 이미지 URL 목록 (imageList)
 */
data class ReceivedMindRecordDetail(
    val mindRecordId: Long,
    val title: String?,
    val recordDate: String?,
    val content: String?,
    val questionContent: String?,
    val category: String?,
    val imageUrls: List<String>
)
