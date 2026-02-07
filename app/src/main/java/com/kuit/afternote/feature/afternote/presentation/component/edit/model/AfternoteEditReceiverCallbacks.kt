package com.kuit.afternote.feature.afternote.presentation.component.edit.model

import androidx.compose.runtime.Immutable

/**
 * 수신자 관련 콜백을 묶는 data class
 */
@Immutable
data class AfternoteEditReceiverCallbacks(
    val onAddClick: () -> Unit = {},
    val onItemDeleteClick: (String) -> Unit = {},
    val onItemAdded: (String) -> Unit = {},
    val onTextFieldVisibilityChanged: (Boolean) -> Unit = {}
)
