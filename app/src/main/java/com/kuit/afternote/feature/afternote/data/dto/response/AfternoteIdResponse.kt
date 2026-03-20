package com.kuit.afternote.feature.afternote.data.dto.response

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

/**
 * Server response data for POST /afternotes and PATCH /afternotes/{id}.
 * Accepts both snake_case (afternote_id) and camelCase (afternoteId) from API.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class AfternoteIdResponse(
    @SerialName("afternoteId")
    @JsonNames("afternote_id") val afternoteId: Long,
)

