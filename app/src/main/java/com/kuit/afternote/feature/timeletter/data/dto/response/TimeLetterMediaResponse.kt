package com.kuit.afternote.feature.timeletter.data.dto.response

import com.kuit.afternote.feature.timeletter.data.dto.MediaType
import kotlinx.serialization.Serializable

@Serializable
data class TimeLetterMediaResponse(
    val id : Long,
    val mediaType: MediaType,
    val mediaUrl: String
)
