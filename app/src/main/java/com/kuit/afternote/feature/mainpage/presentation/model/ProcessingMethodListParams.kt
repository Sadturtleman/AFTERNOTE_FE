package com.kuit.afternote.feature.mainpage.presentation.model

/**
 * ProcessingMethodList 함수의 매개변수를 묶는 data class
 */
data class ProcessingMethodListParams(
    val items: List<ProcessingMethodItem>,
    val onAddClick: () -> Unit = {},
    val onItemMoreClick: (String) -> Unit = {},
    val onItemEditClick: (String) -> Unit = {},
    val onItemDeleteClick: (String) -> Unit = {},
    val onItemAdded: (String) -> Unit = {},
    val onTextFieldVisibilityChanged: (Boolean) -> Unit = {},
    val initialShowTextField: Boolean = false,
    val initialExpandedItemId: String? = null
)
