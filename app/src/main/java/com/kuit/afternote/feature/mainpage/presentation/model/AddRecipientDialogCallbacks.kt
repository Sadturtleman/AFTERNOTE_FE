package com.kuit.afternote.feature.mainpage.presentation.model

import androidx.compose.runtime.Immutable

/**
 * AddRecipientDialog의 콜백들을 묶는 data class
 */
@Immutable
data class AddRecipientDialogCallbacks(
    val onDismiss: () -> Unit = {},
    val onAddClick: () -> Unit = {},
    val onRelationshipSelected: (String) -> Unit = {},
    val onImportContactsClick: () -> Unit = {}
)
