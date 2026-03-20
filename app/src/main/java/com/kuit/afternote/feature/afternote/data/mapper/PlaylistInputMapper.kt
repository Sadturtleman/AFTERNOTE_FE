package com.kuit.afternote.feature.afternote.data.mapper

import com.kuit.afternote.feature.afternote.data.dto.AfternotePlaylist
import com.kuit.afternote.feature.afternote.data.dto.AfternoteSong
import com.kuit.afternote.feature.afternote.domain.model.playlist.PlaylistInput
import com.kuit.afternote.feature.afternote.domain.model.playlist.SongInput

fun PlaylistInput.toDto() =
    AfternotePlaylist(
        profilePhoto = profilePhoto,
        atmosphere = atmosphere,
        memorialPhotoUrl = memorialPhotoUrl,
        songs = songs.map { it.toDto() },
        memorialVideo = memorialVideo?.toDto(),
    )

fun SongInput.toDto() =
    AfternoteSong(
        id = id,
        title = title,
        artist = artist,
        coverUrl = coverUrl,
    )