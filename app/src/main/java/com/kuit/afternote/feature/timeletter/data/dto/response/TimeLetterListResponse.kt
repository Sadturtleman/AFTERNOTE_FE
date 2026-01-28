package com.kuit.afternote.feature.timeletter.data.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class TimeLetterListResponse(
    val timeLetters: List<TimeLetterResponse>,
    val totalCount: Int
)
