package com.kuit.afternote.feature.afternote.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class AfternoteSong(
    val id: Long? = null,
    val title: String,
    val artist: String,
    val coverUrl: String? = null,
)
