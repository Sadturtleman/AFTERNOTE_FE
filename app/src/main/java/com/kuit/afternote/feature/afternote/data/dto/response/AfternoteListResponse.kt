package com.kuit.afternote.feature.afternote.data.dto.response

import com.kuit.afternote.feature.afternote.data.dto.AfternoteListItem
import kotlinx.serialization.Serializable

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
