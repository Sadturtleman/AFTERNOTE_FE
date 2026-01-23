package com.kuit.afternote.feature.onboarding.presentation.viewmodel

/**
 * 로그아웃 화면 UiState.
 */
data class LogoutUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val logoutSuccess: Boolean = false
)
