package com.kuit.afternote.feature.mainpage.presentation.model

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Immutable

/**
 * GalleryAndFileEditContent 함수의 매개변수를 묶는 data class
 */
@Immutable
data class GalleryAndFileEditContentParams(
    val messageState: TextFieldState,
    val infoMethodSection: InfoMethodSection,
    val recipientSection: RecipientSection? = null,
    val processingMethodSection: ProcessingMethodSection = ProcessingMethodSection()
)
