package com.kuit.afternote.feature.afternote.domain.model.playlist

data class CreatePlaylistInput(
    val title: String,
    val playlist: PlaylistInput,
    val receiverIds: List<Long> = emptyList(),
)
