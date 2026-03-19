package com.kuit.afternote.feature.afternote.data.dto.response

import com.kuit.afternote.feature.afternote.data.dto.AfternoteListItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Server response form for GET /afternotes.
 * Matches API: data.content[], page, size, hasNext.
 */
@Serializable
data class AfternoteListResponse(
    @SerialName("content") val content: List<AfternoteListItem> = emptyList(),
    @SerialName("page") val page: Int = 0,
    @SerialName("size") val size: Int = 10,
    @SerialName("hasNext") val hasNext: Boolean = false,
)
