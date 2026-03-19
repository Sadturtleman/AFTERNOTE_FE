package com.kuit.afternote.feature.afternote.data.dto.request

import com.kuit.afternote.feature.afternote.data.dto.AfternoteReceiverRef
import kotlinx.serialization.Serializable

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
