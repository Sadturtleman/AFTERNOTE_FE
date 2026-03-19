package com.kuit.afternote.feature.afternote.data.dto.request

import com.kuit.afternote.feature.afternote.data.dto.AfternoteCredentials
import com.kuit.afternote.feature.afternote.data.dto.AfternotePlaylist
import com.kuit.afternote.feature.afternote.data.dto.AfternoteReceiverRef
import kotlinx.serialization.Serializable

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
