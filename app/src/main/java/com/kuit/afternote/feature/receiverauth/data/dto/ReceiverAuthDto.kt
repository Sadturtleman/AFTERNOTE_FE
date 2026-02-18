package com.kuit.afternote.feature.receiverauth.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 수신자 인증번호 기반 콘텐츠 조회 API DTO. (스웨거 기준)
 *
 * - POST /api/receiver-auth/verify (인증번호 검증)
 * - GET /api/receiver-auth/time-letters, mind-records, after-notes (목록 조회)
 */

// --- POST /api/receiver-auth/verify (인증번호 검증) ---

@Serializable
data class ReceiverAuthVerifyRequestDto(
    @SerialName("authCode") val authCode: String
)

@Serializable
data class ReceiverAuthVerifyResponseDto(
    @SerialName("receiverId") val receiverId: Long,
    @SerialName("receiverName") val receiverName: String? = null,
    @SerialName("senderName") val senderName: String? = null,
    @SerialName("relation") val relation: String? = null
)

// --- GET /api/receiver-auth/time-letters (타임레터 목록) ---

@Serializable
data class ReceivedTimeLetterAuthResponseDto(
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
data class ReceivedTimeLetterListAuthResponseDto(
    @SerialName("timeLetters") val timeLetters: List<ReceivedTimeLetterAuthResponseDto> = emptyList(),
    @SerialName("totalCount") val totalCount: Int = 0
)

// --- GET /api/receiver-auth/mind-records (마인드레코드 목록) ---

@Serializable
data class ReceivedMindRecordAuthResponseDto(
    @SerialName("id") val id: Long,
    @SerialName("type") val type: String? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("recordDate") val recordDate: String? = null,
    @SerialName("isDraft") val isDraft: Boolean = false,
    @SerialName("senderName") val senderName: String? = null,
    @SerialName("createdAt") val createdAt: String? = null
)

@Serializable
data class ReceivedMindRecordListAuthResponseDto(
    @SerialName("mindRecords") val mindRecords: List<ReceivedMindRecordAuthResponseDto> = emptyList(),
    @SerialName("totalCount") val totalCount: Int = 0
)

// --- GET /api/receiver-auth/after-notes (애프터노트 목록) ---

@Serializable
data class ReceivedAfternoteAuthResponseDto(
    @SerialName("id") val id: Long,
    @SerialName("title") val title: String? = null,
    @SerialName("category") val category: String? = null,
    @SerialName("leaveMessage") val leaveMessage: String? = null,
    @SerialName("senderId") val senderId: Long? = null,
    @SerialName("senderName") val senderName: String? = null,
    @SerialName("createdAt") val createdAt: String? = null
)

@Serializable
data class ReceivedAfternoteListAuthResponseDto(
    @SerialName("afternotes") val afternotes: List<ReceivedAfternoteAuthResponseDto> = emptyList(),
    @SerialName("totalCount") val totalCount: Int = 0
)
