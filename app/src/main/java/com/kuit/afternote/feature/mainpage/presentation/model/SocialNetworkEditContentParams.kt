package com.kuit.afternote.feature.mainpage.presentation.model

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Immutable

/**
 * SocialNetworkEditContent 함수의 매개변수를 묶는 data class
 */
@Immutable
data class SocialNetworkEditContentParams(
    val messageState: TextFieldState,
    val accountSection: AccountSection,
    val processingMethodSection: ProcessingMethodSection = ProcessingMethodSection()
)
