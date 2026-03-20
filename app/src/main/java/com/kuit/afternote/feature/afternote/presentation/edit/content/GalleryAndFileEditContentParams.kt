package com.kuit.afternote.feature.afternote.presentation.edit.content

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Immutable
import com.kuit.afternote.feature.afternote.presentation.edit.model.AfternoteEditReceiverSection
import com.kuit.afternote.feature.afternote.presentation.edit.model.ProcessingMethodSection

/**
 * GalleryAndFileEditContent 함수의 매개변수를 묶는 data class
 */
@Immutable
data class GalleryAndFileEditContentParams(
    val messageState: TextFieldState,
    val recipientSection: AfternoteEditReceiverSection,
    val processingMethodSection: ProcessingMethodSection = ProcessingMethodSection(),
)
