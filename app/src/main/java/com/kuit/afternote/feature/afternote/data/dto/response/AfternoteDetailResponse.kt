package com.kuit.afternote.feature.afternote.data.dto.response

import com.kuit.afternote.feature.afternote.data.dto.AfternoteCredentials
import com.kuit.afternote.feature.afternote.data.dto.AfternoteDetailReceiver
import com.kuit.afternote.feature.afternote.data.dto.AfternotePlaylist
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

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
