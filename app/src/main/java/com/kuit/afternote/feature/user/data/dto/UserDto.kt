package com.kuit.afternote.feature.user.data.dto

import kotlinx.serialization.SerialName
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

// --- GET /users/receivers/{receiverId}/daily-questions (time-letters, after-notes are in Received API) ---

@Serializable
data class DailyQuestionAnswerItemDto(
    @SerialName("dailyQuestionAnswerId") val dailyQuestionAnswerId: Long,
    @SerialName("question") val question: String,
    @SerialName("answer") val answer: String,
    @SerialName("recordDate") val recordDate: String
)

@Serializable
data class ReceiverDailyQuestionsResponseDto(
    @SerialName("items") val items: List<DailyQuestionAnswerItemDto>,
    @SerialName("hasNext") val hasNext: Boolean
)

// --- Image API (POST /images/presigned-url) ---

/** Request for S3 presigned URL. directory: profiles | timeletters | afternotes. */
@Serializable
data class PresignedUrlRequestDto(
    @SerialName("directory") val directory: String,
    @SerialName("extension") val extension: String
)

/** Response: presignedUrl (PUT target), imageUrl (use in PATCH profile), contentType. */
@Serializable
data class PresignedUrlResponseDto(
    @SerialName("presignedUrl") val presignedUrl: String,
    @SerialName("imageUrl") val imageUrl: String,
    @SerialName("contentType") val contentType: String
)
