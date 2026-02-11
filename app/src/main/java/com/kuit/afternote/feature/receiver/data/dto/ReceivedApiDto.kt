package com.kuit.afternote.feature.receiver.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 수신자(Received) API 요청/응답 DTO. (스웨거 기준)
 *
 * - POST /api/received/time-letters, /api/received/mind-records (수신자 등록)
 * - GET /api/received/{receiverId}/time-letters, mind-records, after-notes (수신 목록 조회)
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

// --- GET /api/received/{receiverId}/time-letters (수신한 타임레터 목록) ---

@Serializable
data class ReceivedTimeLetterResponseDto(
    @SerialName("id") val id: Long,
    @SerialName("title") val title: String? = null,
    @SerialName("content") val content: String? = null,
    @SerialName("sendAt") val sendAt: String? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("senderName") val senderName: String? = null,
    @SerialName("deliveredAt") val deliveredAt: String? = null,
    @SerialName("createdAt") val createdAt: String? = null
)

@Serializable
data class ReceivedTimeLetterListResponseDto(
    @SerialName("timeLetters") val timeLetters: List<ReceivedTimeLetterResponseDto> = emptyList(),
    @SerialName("totalCount") val totalCount: Int = 0
)

// --- GET /api/received/{receiverId}/mind-records (수신한 마인드레코드 목록) ---

@Serializable
data class ReceivedMindRecordResponseDto(
    @SerialName("mindRecordId") val mindRecordId: Long,
    @SerialName("sourceType") val sourceType: String? = null,
    @SerialName("content") val content: String? = null,
    @SerialName("recordDate") val recordDate: String? = null
)

@Serializable
data class ReceivedMindRecordListResponseDto(
    @SerialName("mindRecords") val mindRecords: List<ReceivedMindRecordResponseDto> = emptyList(),
    @SerialName("totalCount") val totalCount: Int = 0
)

// --- GET /api/received/{receiverId}/after-notes (수신한 애프터노트 목록) ---

@Serializable
data class ReceivedAfternoteResponseDto(
    @SerialName("afternoteId") val afternoteId: Long? = null,
    @SerialName("sourceType") val sourceType: String? = null,
    @SerialName("lastUpdatedAt") val lastUpdatedAt: String? = null
)

@Serializable
data class ReceivedAfternoteListResponseDto(
    @SerialName("afternotes") val afternotes: List<ReceivedAfternoteResponseDto> = emptyList(),
    @SerialName("totalCount") val totalCount: Int = 0
)
