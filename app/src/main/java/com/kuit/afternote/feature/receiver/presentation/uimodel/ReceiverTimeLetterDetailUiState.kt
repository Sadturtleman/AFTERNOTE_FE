package com.kuit.afternote.feature.receiver.presentation.uimodel

import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.feature.receiver.domain.entity.ReceivedTimeLetter

/**
 * 수신자 타임레터 상세 화면 UI 상태.
 */
data class ReceiverTimeLetterDetailUiState(
    val letter: ReceivedTimeLetter? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val selectedBottomNavItem: BottomNavItem = BottomNavItem.TIME_LETTER
)
