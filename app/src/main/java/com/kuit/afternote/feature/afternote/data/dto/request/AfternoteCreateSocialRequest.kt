package com.kuit.afternote.feature.afternote.data.dto.request

import com.kuit.afternote.feature.afternote.data.dto.AfternoteCredentials
import com.kuit.afternote.feature.afternote.data.dto.AfternoteReceiverRef
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Server request form for POST /afternotes (SOCIAL category).
 * Matches API: category, title, processMethod, actions, leaveMessage, credentials, receivers.
 */
@Serializable
data class AfternoteCreateSocialRequest(
    @SerialName("category") val category: String = "SOCIAL",
    @SerialName("title") val title: String,
    @SerialName("processMethod") val processMethod: String,
    @SerialName("actions") val actions: List<String>,
    @SerialName("leaveMessage") val leaveMessage: String? = null,
    @SerialName("credentials") val credentials: AfternoteCredentials? = null,
    @SerialName("receivers") val receivers: List<AfternoteReceiverRef> = emptyList(),
)
