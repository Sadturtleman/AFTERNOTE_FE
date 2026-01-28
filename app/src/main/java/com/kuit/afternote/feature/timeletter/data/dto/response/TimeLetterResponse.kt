package com.kuit.afternote.feature.timeletter.data.dto.response

import com.kuit.afternote.feature.timeletter.data.dto.TimeLetterStatus
import kotlinx.serialization.Serializable

@Serializable
data class TimeLetterResponse(
    val id: Long,
    val title: String,
    val content: String,
    val sendAt: String,
    val status: TimeLetterStatus,
    val mediaList: List<TimeLetterMediaResponse>,
    val createdAt: String,
    val updatedAt: String
)
