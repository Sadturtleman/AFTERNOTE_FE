package com.kuit.afternote.feature.user.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * User API DTOs. (스웨거 기준)
 */

@Serializable
data class UserResponse(
    @SerialName("name") val name: String,
    @SerialName("email") val email: String,
    @SerialName("phone") val phone: String? = null,
    @SerialName("profileImageUrl") val profileImageUrl: String? = null
)

@Serializable
data class UserUpdateProfileRequest(
    @SerialName("name") val name: String? = null,
    @SerialName("phone") val phone: String? = null,
    @SerialName("profileImageUrl") val profileImageUrl: String? = null
)

/** GET /users/push-settings 응답 data. 푸시 알림 수신 설정 (timeLetter, mindRecord, afterNote). */
@Serializable
data class UserPushSettingResponse(
    @SerialName("timeLetter") val timeLetter: Boolean,
    @SerialName("mindRecord") val mindRecord: Boolean,
    @SerialName("afterNote") val afterNote: Boolean
)

@Serializable
data class UserUpdatePushSettingRequest(
    @SerialName("timeLetter") val timeLetter: Boolean? = null,
    @SerialName("mindRecord") val mindRecord: Boolean? = null,
    @SerialName("afterNote") val afterNote: Boolean? = null
)

// --- Receivers (GET /users/receivers, POST /users/receivers, GET /users/receivers/{receiverId}) ---

@Serializable
data class ReceiverItemDto(
    @SerialName("receiverId") val receiverId: Long,
    @SerialName("name") val name: String,
    @SerialName("relation") val relation: String
)

@Serializable
data class ReceiversListResponseDto(
    @SerialName("receivers") val receivers: List<ReceiverItemDto>
)

@Serializable
data class RegisterReceiverRequestDto(
    @SerialName("name") val name: String,
    @SerialName("phone") val phone: String? = null,
    @SerialName("relation") val relation: String,
    @SerialName("email") val email: String? = null
)

@Serializable
data class RegisterReceiverResponseDto(
    @SerialName("receiverId") val receiverId: Long
)

@Serializable
data class ReceiverDetailResponseDto(
    @SerialName("receiverId") val receiverId: Long,
    @SerialName("name") val name: String,
    @SerialName("relation") val relation: String,
    @SerialName("phone") val phone: String? = null,
    @SerialName("email") val email: String? = null,
    @SerialName("dailyQuestionCount") val dailyQuestionCount: Int,
    @SerialName("timeLetterCount") val timeLetterCount: Int,
    @SerialName("afterNoteCount") val afterNoteCount: Int
)

// --- GET /users/receivers/{receiverId}/daily-questions ---

@Serializable
data class DailyQuestionAnswerItemDto(
    @SerialName("dailyQuestionAnswerId") val dailyQuestionAnswerId: Long,
    @SerialName("question") val question: String,
    @SerialName("answer") val answer: String,
    @SerialName("createdAt") val createdAt: String
)

@Serializable
data class ReceiverDailyQuestionsResponseDto(
    @SerialName("items") val items: List<DailyQuestionAnswerItemDto>
)

// --- GET /users/receivers/{receiverId}/time-letters ---

@Serializable
data class ReceiverTimeLetterItemDto(
    @SerialName("timeLetterId") val timeLetterId: Long,
    @SerialName("receiverName") val receiverName: String,
    @SerialName("sendAt") val sendAt: String,
    @SerialName("title") val title: String,
    @SerialName("content") val content: String
)

@Serializable
data class ReceiverTimeLettersResponseDto(
    @SerialName("items") val items: List<ReceiverTimeLetterItemDto>
)

// --- GET /users/receivers/{receiverId}/after-notes ---

@Serializable
data class ReceiverAfterNoteSourceItemDto(
    @SerialName("sourceType") val sourceType: String,
    @SerialName("lastUpdatedAt") val lastUpdatedAt: String
)

@Serializable
data class ReceiverAfterNotesResponseDto(
    @SerialName("items") val items: List<ReceiverAfterNoteSourceItemDto>
)
