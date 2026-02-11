package com.kuit.afternote.feature.afternote.presentation.component.edit.model

import androidx.compose.runtime.Immutable

/**
 * 수신자 관련 콜백을 묶는 data class
 */
@Immutable
<<<<<<<< HEAD:app/src/main/java/com/kuit/afternote/feature/afternote/presentation/component/edit/model/MainPageEditReceiverCallbacks.kt
data class MainPageEditReceiverCallbacks(
========
data class AfternoteEditReceiverCallbacks(
>>>>>>>> 18f1a0c82c9329353ff9254b226743d587b4b33f:app/src/main/java/com/kuit/afternote/feature/afternote/presentation/component/edit/model/AfternoteEditReceiverCallbacks.kt
    val onAddClick: () -> Unit = {},
    val onItemDeleteClick: (String) -> Unit = {},
    val onItemAdded: (String) -> Unit = {},
    val onTextFieldVisibilityChanged: (Boolean) -> Unit = {}
)
