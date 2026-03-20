package com.kuit.afternote.feature.afternote.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AfternoteSong(
    @SerialName("id") val id: Long? = null,
    @SerialName("title") val title: String,
    @SerialName("artist") val artist: String,
    @SerialName("coverUrl") val coverUrl: String? = null,
)
