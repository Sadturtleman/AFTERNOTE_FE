package com.kuit.afternote.feature.onboarding.presentation.viewmodel

/**
 * 토큰 재발급 화면 UiState.
 */
data class ReissueUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val reissueSuccess: Boolean = false
)
