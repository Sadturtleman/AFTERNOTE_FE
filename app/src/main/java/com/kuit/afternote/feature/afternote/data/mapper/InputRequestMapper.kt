package com.kuit.afternote.feature.afternote.data.mapper

import com.kuit.afternote.feature.afternote.data.dto.AfternoteReceiverRef
import com.kuit.afternote.feature.afternote.data.dto.request.AfternoteCreateGalleryRequest
import com.kuit.afternote.feature.afternote.data.dto.request.AfternoteCreatePlaylistRequest
import com.kuit.afternote.feature.afternote.data.dto.request.AfternoteCreateSocialRequest
import com.kuit.afternote.feature.afternote.data.dto.request.AfternoteUpdateRequest
import com.kuit.afternote.feature.afternote.domain.model.CreateGalleryInput
import com.kuit.afternote.feature.afternote.domain.model.CreateSocialInput
import com.kuit.afternote.feature.afternote.domain.model.UpdateRequestInput
import com.kuit.afternote.feature.afternote.domain.model.playlist.CreatePlaylistInput

fun UpdateRequestInput.toRequest() =
    AfternoteUpdateRequest(
        category = category,
        title = title,
        processMethod = processMethod,
        actions = actions,
        leaveMessage = leaveMessage,
        credentials = credentials?.toDto(),
        receivers = receivers?.toDto(),
        playlist = playlist?.toDto(),
    )

internal fun CreateSocialInput.toRequest() =
    AfternoteCreateSocialRequest(
        category = "SOCIAL",
        title = title,
        processMethod = processMethod,
        actions = actions,
        leaveMessage = leaveMessage,
        credentials = credentials?.toDto(),
        receivers = receiverIds.map { AfternoteReceiverRef(receiverId = it) },
    )

internal fun CreateGalleryInput.toRequest() =
    AfternoteCreateGalleryRequest(
        category = "GALLERY",
        title = title,
        processMethod = processMethod,
        actions = actions,
        leaveMessage = leaveMessage,
        receivers = receiverIds.map { AfternoteReceiverRef(receiverId = it) },
    )

internal fun CreatePlaylistInput.toRequest() =
    AfternoteCreatePlaylistRequest(
        category = "PLAYLIST",
        title = title,
        playlist = playlist.toDto(),
        receivers = receiverIds.map { AfternoteReceiverRef(receiverId = it) },
    )
