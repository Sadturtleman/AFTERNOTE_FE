package com.kuit.afternote.feature.timeletter.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * TimeLetter API DTOs. (스웨거 기준)
 */

@Serializable
enum class TimeLetterStatus {
    DRAFT,
    SCHEDULED,
    SENT
}

@Serializable
enum class TimeLetterMediaType {
    IMAGE,
    VIDEO,
    AUDIO
}

@Serializable
data class TimeLetterMediaRequest(
    @SerialName("mediaType") val mediaType: TimeLetterMediaType,
    @SerialName("mediaUrl") val mediaUrl: String
)

@Serializable
data class TimeLetterCreateRequest(
    @SerialName("title") val title: String? = null,
    @SerialName("content") val content: String? = null,
    @SerialName("sendAt") val sendAt: String? = null,
    @SerialName("status") val status: TimeLetterStatus,
    @SerialName("mediaList") val mediaList: List<TimeLetterMediaRequest>? = null,
    @SerialName("receiverIds") val receiverIds: List<Long> = emptyList(),
    @SerialName("deliveredAt") val deliveredAt: String? = null
)

@Serializable
data class TimeLetterMediaResponse(
    @SerialName("id") val id: Long,
    @SerialName("mediaType") val mediaType: TimeLetterMediaType,
    @SerialName("mediaUrl") val mediaUrl: String
)

@Serializable
data class TimeLetterResponse(
    @SerialName("id") val id: Long,
    @SerialName("title") val title: String? = null,
    @SerialName("content") val content: String? = null,
    @SerialName("sendAt") val sendAt: String? = null,
    @SerialName("status") val status: TimeLetterStatus,
    @SerialName("mediaList") val mediaList: List<TimeLetterMediaResponse>? = null,
    @SerialName("createdAt") val createdAt: String? = null,
    @SerialName("updatedAt") val updatedAt: String? = null
)

@Serializable
data class TimeLetterListResponse(
    @SerialName("timeLetters") val timeLetters: List<TimeLetterResponse>,
    @SerialName("totalCount") val totalCount: Int
)

@Serializable
data class TimeLetterDeleteRequest(
    @SerialName("timeLetterIds") val timeLetterIds: List<Long>
)

@Serializable
data class TimeLetterUpdateRequest(
    @SerialName("title") val title: String? = null,
    @SerialName("content") val content: String? = null,
    @SerialName("sendAt") val sendAt: String? = null,
    @SerialName("status") val status: TimeLetterStatus? = null,
    @SerialName("mediaList") val mediaList: List<TimeLetterMediaRequest>? = null
)
