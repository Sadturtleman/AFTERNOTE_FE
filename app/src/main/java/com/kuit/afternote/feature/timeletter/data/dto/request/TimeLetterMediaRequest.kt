package com.kuit.afternote.feature.timeletter.data.dto.request

import com.kuit.afternote.feature.timeletter.data.dto.MediaType
import kotlinx.serialization.Serializable

@Serializable
data class TimeLetterMediaRequest(
    val mediaType: MediaType,
    val mediaUrl: String
)

