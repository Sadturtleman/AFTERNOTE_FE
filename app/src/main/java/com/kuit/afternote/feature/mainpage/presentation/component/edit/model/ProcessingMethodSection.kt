package com.kuit.afternote.feature.mainpage.presentation.component.edit.model

import androidx.compose.runtime.Immutable

/**
 * 처리 방법 리스트 섹션 (공통)
 */
@Immutable
data class ProcessingMethodSection(
    val items: List<ProcessingMethodItem> = emptyList(),
    val callbacks: ProcessingMethodCallbacks = ProcessingMethodCallbacks()
)
