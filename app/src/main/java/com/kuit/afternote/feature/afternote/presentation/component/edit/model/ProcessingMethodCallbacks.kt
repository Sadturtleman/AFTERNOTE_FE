package com.kuit.afternote.feature.afternote.presentation.component.edit.model

import androidx.compose.runtime.Immutable

/**
 * 처리 방법 리스트 관련 콜백을 묶는 data class
 */
@Immutable
data class ProcessingMethodCallbacks(
    val onItemMoreClick: (String) -> Unit = {},
    val onItemEditClick: (String) -> Unit = {},
    val onItemDeleteClick: (String) -> Unit = {},
    val onItemAdded: (String) -> Unit = {},
    val onTextFieldVisibilityChanged: (Boolean) -> Unit = {}
)
