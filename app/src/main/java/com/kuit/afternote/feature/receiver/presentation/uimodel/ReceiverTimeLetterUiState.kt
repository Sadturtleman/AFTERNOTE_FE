package com.kuit.afternote.feature.receiver.presentation.uimodel

import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.feature.receiver.domain.entity.ReceivedTimeLetter

/**
 * 수신자 타임레터 목록 화면 UI 상태.
 */
data class ReceiverTimeLetterUiState(
    val timeLetters: List<ReceivedTimeLetter> = emptyList(),
    val totalCount: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val selectedBottomNavItem: BottomNavItem = BottomNavItem.TIME_LETTER
)
