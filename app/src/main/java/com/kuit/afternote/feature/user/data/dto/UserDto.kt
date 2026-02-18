package com.kuit.afternote.feature.user.data.dto

import kotlinx.serialization.Serializable

/**
 * User API DTOs. (스웨거 기준)
 */

/**
 * GET /users/me (and any API returning user profile) response.
 *
 * When the profile image bucket is private, [profileImageUrl] must be a **GET pre-signed URL**
 * (with X-Amz-Signature etc. in query params). If the server returns a raw S3 URL, the app gets
 * HTTP 403 when loading the image (Coil/ProfileImage).
 */
@Serializable
data class UserResponse(
    val name: String,
    val email: String,
    val phone: String? = null,
    val profileImageUrl: String? = null
)

@Serializable
data class UserUpdateProfileRequest(
    val name: String? = null,
    val phone: String? = null,
    val profileImageUrl: String? = null
)

/** GET /users/push-settings 응답 data. 푸시 알림 수신 설정 (timeLetter, mindRecord, afterNote). */
@Serializable
data class UserPushSettingResponse(
    val timeLetter: Boolean,
    val mindRecord: Boolean,
    val afterNote: Boolean
)

@Serializable
data class UserUpdatePushSettingRequest(
    val timeLetter: Boolean? = null,
    val mindRecord: Boolean? = null,
    val afterNote: Boolean? = null
)

// --- Receivers (GET /users/receivers, POST /users/receivers, GET /users/receivers/{receiverId}) ---

@Serializable
data class ReceiverItemDto(
    val receiverId: Long,
    val name: String,
    val relation: String
)

@Serializable
data class RegisterReceiverRequestDto(
    val name: String,
    val phone: String? = null,
    val relation: String,
    val email: String? = null
)

@Serializable
data class RegisterReceiverResponseDto(
    val receiverId: Long
)

@Serializable
data class ReceiverDetailResponseDto(
    val receiverId: Long,
    val name: String,
    val relation: String,
    val phone: String? = null,
    val email: String? = null,
    val dailyQuestionCount: Int = 0,
    val timeLetterCount: Int = 0,
    val afterNoteCount: Int = 0
)

// --- GET /users/receivers/{receiverId}/daily-questions (time-letters, after-notes are in Received API) ---

@Serializable
data class DailyQuestionAnswerItemDto(
    val dailyQuestionAnswerId: Long,
    val question: String,
    val answer: String,
    val recordDate: String
)

@Serializable
data class ReceiverDailyQuestionsResponseDto(
    val items: List<DailyQuestionAnswerItemDto>,
    val hasNext: Boolean
)

// --- GET /users/delivery-condition, PATCH /users/delivery-condition (전달 조건) ---

/**
 * 전달 조건 타입 - 콘텐츠가 수신자에게 전달되는 조건.
 */
@Serializable
enum class DeliveryConditionTypeDto {
    NONE,
    DEATH_CERTIFICATE,
    INACTIVITY,
    SPECIFIC_DATE
}

/**
 * GET /users/delivery-condition 응답 data. 전달 조건 설정 응답.
 */
@Serializable
data class DeliveryConditionResponseDto(
    val conditionType: DeliveryConditionTypeDto,
    val inactivityPeriodDays: Int? = null,
    val specificDate: String? = null,
    val leaveMessage: String? = null,
    val conditionFulfilled: Boolean,
    val conditionMet: Boolean
)

/**
 * PATCH /users/delivery-condition 요청 body.
 */
@Serializable
data class DeliveryConditionRequestDto(
    val conditionType: DeliveryConditionTypeDto,
    val inactivityPeriodDays: Int? = null,
    val specificDate: String? = null,
    val leaveMessage: String? = null
)

// --- File API (POST /files/presigned-url) ---

/** Request for S3 presigned URL. directory: e.g. profiles, afternotes. extension: e.g. jpg, png. */
@Serializable
data class PresignedUrlRequestDto(
    val directory: String,
    val extension: String
)

/** Response: presignedUrl (PUT target), fileUrl (final URL after upload), contentType. */
@Serializable
data class PresignedUrlResponseDto(
    val presignedUrl: String,
    val fileUrl: String,
    val contentType: String
)
