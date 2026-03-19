package com.kuit.afternote.feature.afternote.data.dto

import kotlinx.serialization.Serializable

/**
 * Single item in list response. Server form: afternoteId, title, category, createdAt.
 */
@Serializable
data class AfternoteListItem(
    val afternoteId: Long,
    val title: String,
    val category: String,
    val createdAt: String,
)
