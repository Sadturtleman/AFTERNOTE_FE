package com.kuit.afternote.feature.afternote.data.dto

import kotlinx.serialization.Serializable

/**
 * Response body for GET /music/search?keyword=.
 * Server returns 200 with tracks array; 400 when keyword is missing.
 */
@Serializable
data class MusicSearchResponseDto(
    val tracks: List<MusicTrackDto> = emptyList()
)

/**
 * Single track in music search response.
 */
@Serializable
data class MusicTrackDto(
    val artist: String,
    val title: String,
    val albumImageUrl: String? = null
)
