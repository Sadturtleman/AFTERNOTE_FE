package com.kuit.afternote.feature.afternote.presentation.edit.processingmethod

import com.kuit.afternote.feature.afternote.presentation.edit.model.ProcessingMethodItem

/**
 * ProcessingMethodList 함수의 매개변수를 묶는 data class
 */
data class ProcessingMethodListParams(
    val items: List<ProcessingMethodItem>,
    val onItemDeleteClick: (String) -> Unit = {},
    val onItemAdded: (String) -> Unit = {},
    val onTextFieldVisibilityChanged: (Boolean) -> Unit = {},
    val onItemEdited: (String, String) -> Unit = { _, _ -> },
    val initialShowTextField: Boolean = false,
    val initialExpandedItemId: String? = null,
)
