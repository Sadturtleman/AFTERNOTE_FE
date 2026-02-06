package com.kuit.afternote.feature.afternote.presentation.component.edit.content

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Immutable
import com.kuit.afternote.feature.afternote.presentation.component.edit.model.AccountSection
import com.kuit.afternote.feature.afternote.presentation.component.edit.model.ProcessingMethodSection

/**
 * SocialNetworkEditContent 함수의 매개변수를 묶는 data class
 */
@Immutable
data class SocialNetworkEditContentParams(
    val messageState: TextFieldState,
    val accountSection: AccountSection,
    val processingMethodSection: ProcessingMethodSection = ProcessingMethodSection()
)
