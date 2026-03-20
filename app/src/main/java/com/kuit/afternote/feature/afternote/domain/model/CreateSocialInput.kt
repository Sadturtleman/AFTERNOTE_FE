package com.kuit.afternote.feature.afternote.domain.model

import com.kuit.afternote.feature.afternote.data.dto.AfternoteReceiverRef
import com.kuit.afternote.feature.afternote.data.dto.request.AfternoteCreateSocialRequest
import com.kuit.afternote.feature.afternote.data.mapper.toDto

data class CreateSocialInput(
    val title: String,
    val processMethod: String,
    val actions: List<String>,
    val leaveMessage: String? = null,
    val credentials: CredentialsInput? = null,
    val receiverIds: List<Long> = emptyList(),
)

fun CreateSocialInput.toRequest() =
    AfternoteCreateSocialRequest(
        category = "SOCIAL",
        title = title,
        processMethod = processMethod,
        actions = actions,
        leaveMessage = leaveMessage,
        credentials = credentials?.toDto(),
        receivers = receiverIds.map { AfternoteReceiverRef(receiverId = it) },
    )
