package com.kuit.afternote.feature.mainpage.presentation.model

import androidx.compose.runtime.Immutable

/**
 * 수신자 관련 콜백을 묶는 data class
 */
@Immutable
data class RecipientCallbacks(
    val onAddClick: () -> Unit = {},
    val onItemEditClick: (String) -> Unit = {},
    val onItemDeleteClick: (String) -> Unit = {},
    val onItemAdded: (String) -> Unit = {},
    val onTextFieldVisibilityChanged: (Boolean) -> Unit = {}
)
