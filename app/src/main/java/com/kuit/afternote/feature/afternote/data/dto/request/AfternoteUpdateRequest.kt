package com.kuit.afternote.feature.afternote.data.dto.request

import com.kuit.afternote.feature.afternote.data.dto.AfternoteCredentials
import com.kuit.afternote.feature.afternote.data.dto.AfternotePlaylist
import com.kuit.afternote.feature.afternote.data.dto.AfternoteReceiverRef
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Partial update request. Title and category are mandatory per server API.
 * Send only fields valid for the afternote's category (SOCIAL: credentials, etc.; GALLERY: receivers; PLAYLIST: playlist).
 */
@Serializable
data class AfternoteUpdateRequest(
    @SerialName("category") val category: String,
    @SerialName("title") val title: String,
    @SerialName("processMethod") val processMethod: String? = null,
    @SerialName("actions") val actions: List<String>? = null,
    @SerialName("leaveMessage") val leaveMessage: String? = null,
    @SerialName("credentials") val credentials: AfternoteCredentials? = null,
    @SerialName("receivers") val receivers: List<AfternoteReceiverRef>? = null,
    @SerialName("playlist") val playlist: AfternotePlaylist? = null,
)
