package com.kuit.afternote.feature.afternote.domain.model

/**
 * Saved processing method entry for an afternote item.
 * Domain representation of a single "처리 방법" (e.g. "게시물 내리기").
 */
data class AfternoteProcessingMethod(
    val id: String,
    val text: String
)
