package com.kuit.afternote.feature.afternote.data.mapper

import com.kuit.afternote.feature.afternote.data.dto.AfternoteCredentials
import com.kuit.afternote.feature.afternote.data.dto.AfternoteMemorialVideo
import com.kuit.afternote.feature.afternote.data.dto.AfternotePlaylist
import com.kuit.afternote.feature.afternote.data.dto.AfternoteReceiverRef
import com.kuit.afternote.feature.afternote.data.dto.AfternoteSong
import com.kuit.afternote.feature.afternote.data.dto.request.AfternoteUpdateRequest
import com.kuit.afternote.feature.afternote.domain.model.AfternoteUpdateRequestInput
import com.kuit.afternote.feature.afternote.domain.model.CredentialsInput
import com.kuit.afternote.feature.afternote.domain.model.ReceiverRefInput
import com.kuit.afternote.feature.afternote.domain.model.playlist.AfternotePlaylistInput
import com.kuit.afternote.feature.afternote.domain.model.playlist.MemorialVideoInput
import com.kuit.afternote.feature.afternote.domain.model.playlist.SongInput

fun AfternotePlaylistInput.toDto() =
    AfternotePlaylist(
        profilePhoto = profilePhoto,
        atmosphere = atmosphere,
        memorialPhotoUrl = memorialPhotoUrl,
        songs = songs.map { it.toDto() },
        memorialVideo = memorialVideo?.toDto(),
    )

fun MemorialVideoInput.toDto() =
    AfternoteMemorialVideo(
        videoUrl = videoUrl,
        thumbnailUrl = thumbnailUrl,
    )

fun SongInput.toDto() =
    AfternoteSong(
        id = id,
        title = title,
        artist = artist,
        coverUrl = coverUrl,
    )

fun AfternoteUpdateRequestInput.toDto() =
    AfternoteUpdateRequest(
        category = category,
        title = title,
        processMethod = processMethod,
        actions = actions,
        leaveMessage = leaveMessage,
        credentials = credentials?.toDto(),
        receivers = receivers?.map { it.toDto() },
        playlist = playlist?.toDto(),
    )

fun CredentialsInput.toDto() =
    AfternoteCredentials(
        id = id,
        password = password,
    )

fun ReceiverRefInput.toDto() =
    AfternoteReceiverRef(
        receiverId = receiverId,
    )
