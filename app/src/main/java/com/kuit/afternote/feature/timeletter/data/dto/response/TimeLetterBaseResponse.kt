package com.kuit.afternote.feature.timeletter.data.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class TimeLetterBaseResponse<T>(
    val status: Int,
    val code: Int,
    val message: String,
    val data: T
)
