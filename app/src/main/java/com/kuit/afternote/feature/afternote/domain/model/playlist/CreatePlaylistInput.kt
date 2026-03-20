package com.kuit.afternote.feature.afternote.domain.model.playlist

import com.kuit.afternote.feature.afternote.data.dto.AfternoteReceiverRef
import com.kuit.afternote.feature.afternote.data.dto.request.AfternoteCreatePlaylistRequest
import com.kuit.afternote.feature.afternote.data.mapper.toDto

data class CreatePlaylistInput(
    val title: String,
    val playlist: PlaylistInput,
    val receiverIds: List<Long> = emptyList(),
)

fun CreatePlaylistInput.toRequest() =
    AfternoteCreatePlaylistRequest(
        category = "PLAYLIST",
        title = title,
        playlist = playlist.toDto(),
        receivers = receiverIds.map { AfternoteReceiverRef(receiverId = it) },
    )
