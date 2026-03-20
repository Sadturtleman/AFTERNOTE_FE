package com.kuit.afternote.feature.afternote.domain.model.playlist

data class PlaylistInput(
    val profilePhoto: String? = null,
    val atmosphere: String? = null,
    val memorialPhotoUrl: String? = null,
    val songs: List<SongInput> = emptyList(),
    val memorialVideo: MemorialVideoInput? = null,
)

data class MemorialVideoInput(
    val videoUrl: String? = null,
    val thumbnailUrl: String? = null,
)

data class SongInput(
    val id: Long? = null,
    val title: String,
    val artist: String,
    val coverUrl: String? = null,
)
