package com.kuit.afternote.feature.afternote.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AfternoteMemorialVideo(
    @SerialName("videoUrl") val videoUrl: String? = null,
    @SerialName("thumbnailUrl") val thumbnailUrl: String? = null,
)
