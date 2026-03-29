package com.kuit.afternote.feature.afternote.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class AfternotePresignedUrlRequestDto(
    val directory: String,
    val extension: String,
)

@Serializable
data class AfternotePresignedUrlResponseDto(
    val presignedUrl: String,
    val fileUrl: String,
    val contentType: String,
)
