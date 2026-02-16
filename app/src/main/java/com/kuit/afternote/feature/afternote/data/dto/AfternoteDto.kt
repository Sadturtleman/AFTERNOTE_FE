package com.kuit.afternote.feature.afternote.data.dto

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

/**
 * Server response form for GET /afternotes.
 * Matches API: data.content[], page, size, hasNext.
 */
@Serializable
data class AfternoteListResponseDto(
    val content: List<AfternoteListItemDto> = emptyList(),
    val page: Int = 0,
    val size: Int = 10,
    val hasNext: Boolean = false
)

/**
 * Single item in list response. Server form: afternoteId, title, category, createdAt.
 */
@Serializable
data class AfternoteListItemDto(
    val afternoteId: Long,
    val title: String,
    val category: String,
    val createdAt: String
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
    val leaveMessage: String? = null,
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
    val leaveMessage: String? = null,
    val receivers: List<AfternoteReceiverRefDto>
)

@Serializable
data class AfternoteCredentialsDto(
    val id: String? = null,
    val password: String? = null
)

@Serializable
data class AfternoteReceiverRefDto(
    val receiverId: Long
)

/**
 * Server response data for POST /afternotes and PATCH /afternotes/{id}.
 * Accepts both snake_case (afternote_id) and camelCase (afternoteId) from API.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class AfternoteIdResponseDto(
    @JsonNames("afternote_id") val afternoteId: Long
)

// --- GET /afternotes/{afternoteId} (detail) ---

/**
 * Server response for GET /afternotes/{afternoteId}.
 * Common fields always present; category-specific fields (credentials, receivers, playlist) are null when not applicable.
 */
@Serializable
data class AfternoteDetailResponseDto(
    val afternoteId: Long,
    val category: String,
    val title: String,
    val createdAt: String = "",
    val updatedAt: String = "",
    val credentials: AfternoteCredentialsDto? = null,
    val receivers: List<AfternoteDetailReceiverDto>? = null,
    val processMethod: String? = null,
    val actions: List<String>? = null,
    val leaveMessage: String? = null,
    val playlist: AfternotePlaylistDto? = null
)

/**
 * Receiver in GET /afternotes/{id} response (GALLERY).
 * API currently returns only receiverId; name/relation are resolved via GET /users/receivers.
 * Accepts optional name/relation/phone if API adds them later.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class AfternoteDetailReceiverDto(
    @JsonNames("receiver_id") val receiverId: Long? = null,
    @JsonNames("receiverName", "receiver_name") val name: String? = null,
    @JsonNames("receiverRelation", "receiver_relation", "relationship") val relation: String? = null,
    @JsonNames("receiverPhone", "receiver_phone", "phoneNumber", "phone_number") val phone: String? = null
)

@Serializable
data class AfternotePlaylistDto(
    val profilePhoto: String? = null,
    val atmosphere: String? = null,
    val songs: List<AfternoteSongDto> = emptyList(),
    val memorialVideo: AfternoteMemorialVideoDto? = null
)

@Serializable
data class AfternoteSongDto(
    val id: Long? = null,
    val title: String,
    val artist: String,
    val coverUrl: String? = null
)

@Serializable
data class AfternoteMemorialVideoDto(
    val videoUrl: String? = null,
    val thumbnailUrl: String? = null
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
 * Partial update request. Title and category are mandatory per server API.
 * Send only fields valid for the afternote's category (SOCIAL: credentials, etc.; GALLERY: receivers; PLAYLIST: playlist).
 */
@Serializable
data class AfternoteUpdateRequestDto(
    val category: String,
    val title: String,
    val processMethod: String? = null,
    val actions: List<String>? = null,
    val leaveMessage: String? = null,
    val credentials: AfternoteCredentialsDto? = null,
    val receivers: List<AfternoteReceiverRefDto>? = null,
    val playlist: AfternotePlaylistDto? = null
)
