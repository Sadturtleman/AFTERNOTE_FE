package com.kuit.afternote.feature.receiver.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 수신자(Received) API 요청 DTO. (스웨거 기준)
 *
 * - POST /api/received/time-letters: 타임레터 수신자 등록
 * - POST /api/received/mind-records: 마인드레코드 수신자 등록
 *
 * 수신 목록 조회는 마스터키(인증번호) 기반 receiver-auth API를 사용합니다.
 */

// --- POST /api/received/time-letters (타임레터 수신자 등록) ---

@Serializable
data class CreateTimeLetterReceiverRequestDto(
    @SerialName("timeLetterID") val timeLetterId: Long,
    @SerialName("receiverIds") val receiverIds: List<Long>,
    @SerialName("deliveredAt") val deliveredAt: String? = null
)

// --- POST /api/received/mind-records (마인드레코드 수신자 등록) ---

@Serializable
data class CreateMindRecordReceiverRequestDto(
    @SerialName("mindRecordId") val mindRecordId: Long,
    @SerialName("receiverIds") val receiverIds: List<Long>
)

