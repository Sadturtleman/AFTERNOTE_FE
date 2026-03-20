package com.kuit.afternote.feature.afternote.data.dto.response

import com.kuit.afternote.feature.afternote.data.dto.AfternoteCredentials
import com.kuit.afternote.feature.afternote.data.dto.AfternoteDetailReceiver
import com.kuit.afternote.feature.afternote.data.dto.AfternotePlaylist
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

/**
 * Server response for GET /afternotes/{afternoteId}.
 * Common fields always present; category-specific fields (credentials, receivers, playlist) are null when not applicable.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class AfternoteDetailResponse(
    @SerialName("afternoteId") val afternoteId: Long,
    @SerialName("category") val category: String,
    @SerialName("title") val title: String,
    @SerialName("createdAt")
    @JsonNames("created_at", "createdAt") val createdAt: String = "",
    @SerialName("updatedAt")
    @JsonNames("updated_at", "updatedAt") val updatedAt: String = "",
    @SerialName("credentials") val credentials: AfternoteCredentials? = null,
    @SerialName("receivers") val receivers: List<AfternoteDetailReceiver>? = null,
    @SerialName("processMethod") val processMethod: String? = null,
    @SerialName("actions") val actions: List<String>? = null,
    @SerialName("leaveMessage") val leaveMessage: String? = null,
    @SerialName("playlist") val playlist: AfternotePlaylist? = null,
)
