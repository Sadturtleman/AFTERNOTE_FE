package com.kuit.afternote.feature.afternote.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Server response form for GET /afternotes.
 * Matches API: data.content[], page, size, hasNext.
 */
@Serializable
data class AfternoteListResponseDto(
    val content: List<AfternoteListItemDto> = emptyList(),
    val page: Int = 0,
    val size: Int = 10,
    @SerialName("hasNext") val hasNext: Boolean = false
)

/**
 * Single item in list response. Server form: afternoteId, title, category, createdAt.
 */
@Serializable
data class AfternoteListItemDto(
    @SerialName("afternoteId") val afternoteId: Long,
    val title: String,
    val category: String,
    @SerialName("createdAt") val createdAt: String
)

/**
 * Server request form for POST /afternotes (SOCIAL category).
 * Matches API: category, title, processMethod, actions, leaveMessage, credentials.
 */
@Serializable
data class AfternoteCreateSocialRequestDto(
    val category: String = "SOCIAL",
    val title: String,
    val processMethod: String,
    val actions: List<String>,
    @SerialName("leaveMessage") val leaveMessage: String? = null,
    val credentials: AfternoteCredentialsDto? = null
)

/**
 * Server request form for POST /afternotes (GALLERY category).
 */
@Serializable
data class AfternoteCreateGalleryRequestDto(
    val category: String = "GALLERY",
    val title: String,
    val processMethod: String,
    val actions: List<String>,
    @SerialName("leaveMessage") val leaveMessage: String? = null,
    val receivers: List<AfternoteReceiverRefDto>
)

@Serializable
data class AfternoteCredentialsDto(
    val id: String? = null,
    val password: String? = null
)

@Serializable
data class AfternoteReceiverRefDto(
    @SerialName("receiverId") val receiverId: Long
)

/**
 * Server response data for POST /afternotes and PATCH /afternotes/{id}.
 */
@Serializable
data class AfternoteIdResponseDto(
    @SerialName("afternote_id") val afternoteId: Long
)
