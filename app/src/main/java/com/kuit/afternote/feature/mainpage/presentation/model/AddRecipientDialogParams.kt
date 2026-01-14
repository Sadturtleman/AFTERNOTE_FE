package com.kuit.afternote.feature.mainpage.presentation.model

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
