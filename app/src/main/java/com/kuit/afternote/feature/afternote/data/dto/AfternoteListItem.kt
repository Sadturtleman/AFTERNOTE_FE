package com.kuit.afternote.feature.afternote.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Single item in list response. Server form: afternoteId, title, category, createdAt.
 */
@Serializable
data class AfternoteListItem(
    @SerialName("afternoteId") val afternoteId: Long,
    @SerialName("title") val title: String,
    @SerialName("category") val category: String,
    @SerialName("createdAt") val createdAt: String,
)
