package com.kuit.afternote.feature.dailyrecord.data.dto

import kotlinx.serialization.Serializable


/**
 * 마음의 기록 API DTOs. (스웨거 기준)
 */

@Serializable
data class MindRecordResponse(
    val id: Int,
    val title: String,
    val content: String
)

@Serializable
data class PostRequestDto(
    val title: String,
    val content: String
)

