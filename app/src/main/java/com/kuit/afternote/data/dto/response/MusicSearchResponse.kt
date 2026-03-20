package com.kuit.afternote.data.dto.response

import com.kuit.afternote.data.dto.MusicTrack
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Response body for GET /music/search?keyword=.
 * Server returns 200 with tracks array; 400 when keyword is missing.
 */
@Serializable
data class MusicSearchResponse(
    @SerialName("tracks") val tracks: List<MusicTrack> = emptyList(),
)
