package com.kuit.afternote.feature.afternote.presentation.component.edit.model

import androidx.compose.runtime.Immutable

/**
 * 수신자 섹션
 */
@Immutable
data class AfternoteEditReceiverSection(
    val afternoteEditReceivers: List<AfternoteEditReceiver> = emptyList(),
    val callbacks: AfternoteEditReceiverCallbacks = AfternoteEditReceiverCallbacks()
)
