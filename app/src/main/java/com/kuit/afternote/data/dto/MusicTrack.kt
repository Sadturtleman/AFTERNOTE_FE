package com.kuit.afternote.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Single track in music search response.
 */
@Serializable
data class MusicTrack(
    @SerialName("artist") val artist: String,
    @SerialName("title") val title: String,
    @SerialName("albumImageUrl") val albumImageUrl: String? = null,
)
