package com.kuit.afternote.feature.afternote.presentation.component.edit.afternoteeditreceiver

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Immutable

/**
 * AddAfternoteEditReceiverDialog 함수의 매개변수를 묶는 data class
 */
@Immutable
data class AddAfternoteEditReceiverDialogParams(
    val afternoteEditReceiverNameState: TextFieldState,
    val phoneNumberState: TextFieldState,
    val relationshipSelectedValue: String,
    val relationshipOptions: List<String>,
    val callbacks: AddAfternoteEditReceiverDialogCallbacks = AddAfternoteEditReceiverDialogCallbacks()
)

/**
 * AddAfternoteEditReceiverDialog의 콜백들을 묶는 data class
 */
@Immutable
data class AddAfternoteEditReceiverDialogCallbacks(
    val onDismiss: () -> Unit = {},
    val onAddClick: () -> Unit = {},
    val onRelationshipSelected: (String) -> Unit = {},
    val onImportContactsClick: () -> Unit = {}
)
