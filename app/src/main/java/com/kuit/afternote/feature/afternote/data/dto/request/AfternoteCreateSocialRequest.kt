package com.kuit.afternote.feature.afternote.data.dto.request

import com.kuit.afternote.feature.afternote.data.dto.AfternoteCredentials
import com.kuit.afternote.feature.afternote.data.dto.AfternoteReceiverRef
import kotlinx.serialization.Serializable

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
