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

// --- GET /afternotes/{afternoteId} (detail) ---

/**
 * Server response for GET /afternotes/{afternoteId}.
 * Common fields always present; category-specific fields (credentials, receivers, playlist) are null when not applicable.
 */
@Serializable
data class AfternoteDetailResponseDto(
    @SerialName("afternoteId") val afternoteId: Long,
    val category: String,
    val title: String,
    @SerialName("createdAt") val createdAt: String = "",
    @SerialName("updatedAt") val updatedAt: String = "",
    val credentials: AfternoteCredentialsDto? = null,
    val receivers: List<AfternoteDetailReceiverDto>? = null,
    val processMethod: String? = null,
    val actions: List<String>? = null,
    @SerialName("leaveMessage") val leaveMessage: String? = null,
    val playlist: AfternotePlaylistDto? = null
)

@Serializable
data class AfternoteDetailReceiverDto(
    val name: String? = null,
    val relation: String? = null,
    val phone: String? = null
)

@Serializable
data class AfternotePlaylistDto(
    val profilePhoto: String? = null,
    val atmosphere: String? = null,
    val songs: List<AfternoteSongDto> = emptyList(),
    @SerialName("memorialVideo") val memorialVideo: AfternoteMemorialVideoDto? = null
)

@Serializable
data class AfternoteSongDto(
    val id: Long? = null,
    val title: String,
    val artist: String,
    @SerialName("coverUrl") val coverUrl: String? = null
)

@Serializable
data class AfternoteMemorialVideoDto(
    @SerialName("videoUrl") val videoUrl: String? = null,
    @SerialName("thumbnailUrl") val thumbnailUrl: String? = null
)

// --- POST /afternotes (PLAYLIST category) ---

@Serializable
data class AfternoteCreatePlaylistRequestDto(
    val category: String = "PLAYLIST",
    val title: String,
    val playlist: AfternotePlaylistDto
)

// --- PATCH /afternotes/{afternoteId} (partial update) ---

/**
 * Partial update request. Only include fields to change; category cannot be changed.
 * Send only fields valid for the afternote's category (SOCIAL: credentials, etc.; GALLERY: receivers; PLAYLIST: playlist).
 */
@Serializable
data class AfternoteUpdateRequestDto(
    val title: String? = null,
    val processMethod: String? = null,
    val actions: List<String>? = null,
    @SerialName("leaveMessage") val leaveMessage: String? = null,
    val credentials: AfternoteCredentialsDto? = null,
    val receivers: List<AfternoteReceiverRefDto>? = null,
    val playlist: AfternotePlaylistDto? = null
)
