package com.kuit.afternote.feature.mainpage.presentation.component.edit.model

import androidx.compose.runtime.Immutable

/**
 * 정보 처리 방법 섹션
 */
@Immutable
data class InfoMethodSection(
    val selectedMethod: InformationProcessingMethod,
    val onMethodSelected: (InformationProcessingMethod) -> Unit
)
