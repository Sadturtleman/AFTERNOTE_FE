package com.kuit.afternote.feature.afternote.data.dto

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

/**
 * Server response form for GET /afternotes.
 * Matches API: data.content[], page, size, hasNext.
 */
@Serializable
data class AfternoteListResponse(
    val content: List<AfternoteListItem> = emptyList(),
    val page: Int = 0,
    val size: Int = 10,
    val hasNext: Boolean = false,
)

/**
 * Single item in list response. Server form: afternoteId, title, category, createdAt.
 */
@Serializable
data class AfternoteListItem(
    val afternoteId: Long,
    val title: String,
    val category: String,
    val createdAt: String,
)

/**
 * Server request form for POST /afternotes (SOCIAL category).
 * Matches API: category, title, processMethod, actions, leaveMessage, credentials, receivers.
 */
@Serializable
data class AfternoteCreateSocialRequest(
    val category: String = "SOCIAL",
    val title: String,
    val processMethod: String,
    val actions: List<String>,
    val leaveMessage: String? = null,
    val credentials: AfternoteCredentials? = null,
    val receivers: List<AfternoteReceiverRef> = emptyList(),
)

/**
 * Server request form for POST /afternotes (GALLERY category).
 * receivers: 수신자 목록 (모든 카테고리에서 가능).
 */
@Serializable
data class AfternoteCreateGalleryRequest(
    val category: String = "GALLERY",
    val title: String,
    val processMethod: String,
    val actions: List<String>,
    val leaveMessage: String? = null,
    val receivers: List<AfternoteReceiverRef>,
)

@Serializable
data class AfternoteCredentials(
    val id: String? = null,
    val password: String? = null,
)

/**
 * 수신자 참조 (API ReceiverRequest). 수신자 목록은 모든 카테고리에서 사용 가능.
 */
@Serializable
data class AfternoteReceiverRef(
    val receiverId: Long,
)

/**
 * Server response data for POST /afternotes and PATCH /afternotes/{id}.
 * Accepts both snake_case (afternote_id) and camelCase (afternoteId) from API.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class AfternoteIdResponse(
    @JsonNames("afternote_id") val afternoteId: Long,
)

// --- GET /afternotes/{afternoteId} (detail) ---

/**
 * Server response for GET /afternotes/{afternoteId}.
 * Common fields always present; category-specific fields (credentials, receivers, playlist) are null when not applicable.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class AfternoteDetailResponse(
    val afternoteId: Long,
    val category: String,
    val title: String,
    @JsonNames("created_at", "createdAt") val createdAt: String = "",
    @JsonNames("updated_at", "updatedAt") val updatedAt: String = "",
    val credentials: AfternoteCredentials? = null,
    val receivers: List<AfternoteDetailReceiver>? = null,
    val processMethod: String? = null,
    val actions: List<String>? = null,
    val leaveMessage: String? = null,
    val playlist: AfternotePlaylist? = null,
)

/**
 * Receiver in GET /afternotes/{id} response (GALLERY).
 * API currently returns only receiverId; name/relation are resolved via GET /users/receivers.
 * Accepts optional name/relation/phone if API adds them later.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class AfternoteDetailReceiver(
    @JsonNames("receiver_id") val receiverId: Long? = null,
    @JsonNames("receiverName", "receiver_name") val name: String? = null,
    @JsonNames("receiverRelation", "receiver_relation", "relationship") val relation: String? = null,
    @JsonNames("receiverPhone", "receiver_phone", "phoneNumber", "phone_number") val phone: String? = null,
)

@Serializable
data class AfternotePlaylist(
    val profilePhoto: String? = null,
    val atmosphere: String? = null,
    val memorialPhotoUrl: String? = null,
    val songs: List<AfternoteSong> = emptyList(),
    val memorialVideo: AfternoteMemorialVideo? = null,
)

@Serializable
data class AfternoteSong(
    val id: Long? = null,
    val title: String,
    val artist: String,
    val coverUrl: String? = null,
)

@Serializable
data class AfternoteMemorialVideo(
    val videoUrl: String? = null,
    val thumbnailUrl: String? = null,
)

// --- POST /afternotes (PLAYLIST category) ---

/**
 * Server request form for POST /afternotes (PLAYLIST category).
 * receivers: 수신자 목록 (모든 카테고리에서 가능).
 */
@Serializable
data class AfternoteCreatePlaylistRequest(
    val category: String = "PLAYLIST",
    val title: String,
    val playlist: AfternotePlaylist,
    val receivers: List<AfternoteReceiverRef> = emptyList(),
)

// --- PATCH /afternotes/{afternoteId} (partial update) ---

/**
 * Partial update request. Title and category are mandatory per server API.
 * Send only fields valid for the afternote's category (SOCIAL: credentials, etc.; GALLERY: receivers; PLAYLIST: playlist).
 */
@Serializable
data class AfternoteUpdateRequest(
    val category: String,
    val title: String,
    val processMethod: String? = null,
    val actions: List<String>? = null,
    val leaveMessage: String? = null,
    val credentials: AfternoteCredentials? = null,
    val receivers: List<AfternoteReceiverRef>? = null,
    val playlist: AfternotePlaylist? = null,
)
