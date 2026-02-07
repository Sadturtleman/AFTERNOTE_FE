package com.kuit.afternote.feature.afternote.presentation.component.edit.processingmethod

import com.kuit.afternote.feature.afternote.presentation.component.edit.model.ProcessingMethodItem

/**
 * ProcessingMethodList 함수의 매개변수를 묶는 data class
 */
data class ProcessingMethodListParams(
    val items: List<ProcessingMethodItem>,
    val onItemMoreClick: (String) -> Unit = {},
    val onItemEditClick: (String) -> Unit = {},
    val onItemDeleteClick: (String) -> Unit = {},
    val onItemAdded: (String) -> Unit = {},
    val onTextFieldVisibilityChanged: (Boolean) -> Unit = {},
    val initialShowTextField: Boolean = false,
    val initialExpandedItemId: String? = null
)
