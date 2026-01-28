package com.kuit.afternote.feature.timeletter.data.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class TimeLetterDeleteRequest(
    val timeLetterIds: List<Int>
)
