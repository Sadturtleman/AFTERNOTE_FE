package com.kuit.afternote.feature.afternote.data.mapper

import com.kuit.afternote.feature.afternote.data.dto.AfternoteCredentials
import com.kuit.afternote.feature.afternote.data.dto.AfternoteMemorialVideo
import com.kuit.afternote.feature.afternote.data.dto.AfternoteReceiverRef
import com.kuit.afternote.feature.afternote.data.dto.request.AfternoteUpdateRequest
import com.kuit.afternote.feature.afternote.domain.model.CredentialsInput
import com.kuit.afternote.feature.afternote.domain.model.ReceiverRefInput
import com.kuit.afternote.feature.afternote.domain.model.UpdateRequestInput
import com.kuit.afternote.feature.afternote.domain.model.playlist.MemorialVideoInput

fun MemorialVideoInput.toDto() =
    AfternoteMemorialVideo(
        videoUrl = videoUrl,
        thumbnailUrl = thumbnailUrl,
    )

fun UpdateRequestInput.toDto() =
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

fun CredentialsInput.toDto() =
    AfternoteCredentials(
        id = id,
        password = password,
    )

private fun ReceiverRefInput.toDto() =
    AfternoteReceiverRef(
        receiverId = receiverId,
    )

private fun List<ReceiverRefInput>?.toDto() =
    this?.map {
        it.toDto()
    }
