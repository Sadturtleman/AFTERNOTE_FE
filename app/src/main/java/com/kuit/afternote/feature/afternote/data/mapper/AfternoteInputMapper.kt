package com.kuit.afternote.feature.afternote.data.mapper

import com.kuit.afternote.feature.afternote.data.dto.AfternoteCredentials
import com.kuit.afternote.feature.afternote.data.dto.AfternoteMemorialVideo
import com.kuit.afternote.feature.afternote.data.dto.AfternoteReceiverRef
import com.kuit.afternote.feature.afternote.domain.model.CredentialsInput
import com.kuit.afternote.feature.afternote.domain.model.ReceiverRefInput
import com.kuit.afternote.feature.afternote.domain.model.playlist.MemorialVideoInput

fun MemorialVideoInput.toDto() =
    AfternoteMemorialVideo(
        videoUrl = videoUrl,
        thumbnailUrl = thumbnailUrl,
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

internal fun List<ReceiverRefInput>?.toDto() =
    this?.map {
        it.toDto()
    }
