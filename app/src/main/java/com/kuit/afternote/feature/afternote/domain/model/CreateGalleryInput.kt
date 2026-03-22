package com.kuit.afternote.feature.afternote.domain.model

import com.kuit.afternote.feature.afternote.data.dto.AfternoteReceiverRef
import com.kuit.afternote.feature.afternote.data.dto.request.AfternoteCreateGalleryRequest

data class CreateGalleryInput(
    val title: String,
    val processMethod: String,
    val actions: List<String>,
    val leaveMessage: String? = null,
    val receiverIds: List<Long> = emptyList(),
)

fun CreateGalleryInput.toRequest() =
    AfternoteCreateGalleryRequest(
        category = "GALLERY",
        title = title,
        processMethod = processMethod,
        actions = actions,
        leaveMessage = leaveMessage,
        receivers = receiverIds.map { AfternoteReceiverRef(receiverId = it) },
    )
