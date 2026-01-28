package com.kuit.afternote.feature.timeletter.data.dto.request

import com.kuit.afternote.feature.timeletter.data.dto.TimeLetterStatus
import kotlinx.serialization.*

@Serializable
data class TimeLetterCreateRequest(
    val title: String,
    val content: String,
    val sendAt: String,
    val status: TimeLetterStatus,
    val mediaList: List<TimeLetterMediaRequest> = emptyList()
)

