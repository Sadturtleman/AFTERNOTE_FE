package com.kuit.afternote.feature.mainpage.presentation.model

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Immutable

/**
 * 계정 정보 섹션
 */
@Immutable
data class AccountSection(
    val idState: TextFieldState,
    val passwordState: TextFieldState,
    val selectedMethod: AccountProcessingMethod,
    val onMethodSelected: (AccountProcessingMethod) -> Unit
)
