package com.kuit.afternote.feature.mainpage.presentation.component.edit.model

import androidx.compose.runtime.Immutable

/**
 * 수신자 섹션
 */
@Immutable
data class MainPageEditReceiverSection(
    val mainPageEditReceivers: List<MainPageEditReceiver> = emptyList(),
    val callbacks: MainPageEditReceiverCallbacks = MainPageEditReceiverCallbacks()
)
