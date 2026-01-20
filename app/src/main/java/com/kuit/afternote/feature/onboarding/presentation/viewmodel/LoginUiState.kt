package com.kuit.afternote.feature.onboarding.presentation.viewmodel

/**
 * 로그인 화면 UiState.
 */
data class LoginUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val loginSuccess: Boolean = false
)
