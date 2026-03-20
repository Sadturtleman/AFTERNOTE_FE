package com.kuit.afternote.feature.afternote.domain.model

data class CreateGalleryInput(
    val title: String,
    val processMethod: String,
    val actions: List<String>,
    val leaveMessage: String? = null,
    val receiverIds: List<Long> = emptyList(),
)
