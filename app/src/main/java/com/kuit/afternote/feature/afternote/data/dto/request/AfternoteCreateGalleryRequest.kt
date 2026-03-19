package com.kuit.afternote.feature.afternote.data.dto.request

import com.kuit.afternote.feature.afternote.data.dto.AfternoteReceiverRef
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Server request form for POST /afternotes (GALLERY category).
 * receivers: 수신자 목록 (모든 카테고리에서 가능).
 */
@Serializable
data class AfternoteCreateGalleryRequest(
    @SerialName("category") val category: String = "GALLERY",
    @SerialName("title") val title: String,
    @SerialName("processMethod") val processMethod: String,
    @SerialName("actions") val actions: List<String>,
    @SerialName("leaveMessage") val leaveMessage: String? = null,
    @SerialName("receivers") val receivers: List<AfternoteReceiverRef>,
)
