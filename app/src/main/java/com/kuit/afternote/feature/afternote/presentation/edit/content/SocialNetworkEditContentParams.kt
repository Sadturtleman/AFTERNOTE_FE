package com.kuit.afternote.feature.afternote.presentation.edit.content

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Immutable
import com.kuit.afternote.feature.afternote.presentation.edit.model.AccountSection
import com.kuit.afternote.feature.afternote.presentation.edit.model.AfternoteEditReceiverSection
import com.kuit.afternote.feature.afternote.presentation.edit.model.ProcessingMethodSection

/**
 * SocialNetworkEditContent 함수의 매개변수를 묶는 data class
 */
@Immutable
data class SocialNetworkEditContentParams(
    val messageState: TextFieldState,
    val accountSection: AccountSection,
    val recipientSection: AfternoteEditReceiverSection? = null,
    val processingMethodSection: ProcessingMethodSection = ProcessingMethodSection(),
)
