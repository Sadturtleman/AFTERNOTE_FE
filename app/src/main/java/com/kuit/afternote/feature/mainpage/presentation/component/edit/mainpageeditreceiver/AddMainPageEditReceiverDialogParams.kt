package com.kuit.afternote.feature.mainpage.presentation.component.edit.mainpageeditreceiver

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Immutable

/**
 * AddMainPageEditReceiverDialog 함수의 매개변수를 묶는 data class
 */
@Immutable
data class AddMainPageEditReceiverDialogParams(
    val mainPageEditReceiverNameState: TextFieldState,
    val phoneNumberState: TextFieldState,
    val relationshipSelectedValue: String,
    val relationshipOptions: List<String>,
    val callbacks: AddMainPageEditReceiverDialogCallbacks = AddMainPageEditReceiverDialogCallbacks()
)

/**
 * AddMainPageEditReceiverDialog의 콜백들을 묶는 data class
 */
@Immutable
data class AddMainPageEditReceiverDialogCallbacks(
    val onDismiss: () -> Unit = {},
    val onAddClick: () -> Unit = {},
    val onRelationshipSelected: (String) -> Unit = {},
    val onImportContactsClick: () -> Unit = {}
)
