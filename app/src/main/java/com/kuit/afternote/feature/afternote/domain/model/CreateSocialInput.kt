package com.kuit.afternote.feature.afternote.domain.model

data class CreateSocialInput(
    val title: String,
    val processMethod: String,
    val actions: List<String>,
    val leaveMessage: String? = null,
    val credentials: CredentialsInput? = null,
    val receiverIds: List<Long> = emptyList(),
)
