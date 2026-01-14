package com.kuit.afternote.feature.mainpage.presentation.model

import androidx.compose.runtime.Immutable

/**
 * 수신자 섹션
 */
@Immutable
data class RecipientSection(
    val recipients: List<Recipient> = emptyList(),
    val callbacks: RecipientCallbacks = RecipientCallbacks()
)
