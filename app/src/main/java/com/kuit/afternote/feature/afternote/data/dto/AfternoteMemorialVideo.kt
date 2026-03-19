package com.kuit.afternote.feature.afternote.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class AfternoteMemorialVideo(
    val videoUrl: String? = null,
    val thumbnailUrl: String? = null,
)
