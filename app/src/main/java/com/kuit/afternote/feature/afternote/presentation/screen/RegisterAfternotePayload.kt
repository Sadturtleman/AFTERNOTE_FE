package com.kuit.afternote.feature.afternote.presentation.screen

import com.kuit.afternote.feature.afternote.domain.model.AfternoteProcessingMethod

/**
 * Payload passed when the user taps "등록" on the afternote edit screen.
 * Used to build a new [com.kuit.afternote.feature.afternote.domain.model.AfternoteItem] and append to the list.
 */
data class RegisterAfternotePayload(
    val serviceName: String,
    val date: String,
    val accountId: String = "",
    val password: String = "",
    val message: String = "",
    val accountProcessingMethod: String = "",
    val informationProcessingMethod: String = "",
    val processingMethods: List<AfternoteProcessingMethod> = emptyList(),
    val galleryProcessingMethods: List<AfternoteProcessingMethod> = emptyList()
)
