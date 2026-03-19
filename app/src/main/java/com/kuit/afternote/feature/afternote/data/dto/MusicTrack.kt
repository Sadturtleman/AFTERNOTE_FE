package com.kuit.afternote.feature.afternote.data.dto

import kotlinx.serialization.Serializable

/**
 * Single track in music search response.
 */
@Serializable
data class MusicTrack(
    val artist: String,
    val title: String,
    val albumImageUrl: String? = null,
)
