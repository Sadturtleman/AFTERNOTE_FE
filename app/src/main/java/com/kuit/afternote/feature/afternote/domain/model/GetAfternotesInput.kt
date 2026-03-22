package com.kuit.afternote.feature.afternote.domain.model

data class GetAfternotesInput(
    val category: String?,
    val page: Int,
    val size: Int,
)
