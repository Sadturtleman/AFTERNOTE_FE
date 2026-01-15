package com.kuit.afternote.feature.mainpage.presentation.component.edit

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Immutable

/**
 * AddRecipientDialog 함수의 매개변수를 묶는 data class
 */
@Immutable
data class AddRecipientDialogParams(
    val recipientNameState: TextFieldState,
    val phoneNumberState: TextFieldState,
    val relationshipSelectedValue: String,
    val relationshipOptions: List<String>,
    val callbacks: AddRecipientDialogCallbacks = AddRecipientDialogCallbacks()
)

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
