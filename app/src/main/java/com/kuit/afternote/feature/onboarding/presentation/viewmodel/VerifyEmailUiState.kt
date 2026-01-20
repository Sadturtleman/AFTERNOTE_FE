package com.kuit.afternote.feature.onboarding.presentation.viewmodel

/**
 * 이메일 인증번호 확인 화면 UiState.
 */
data class VerifyEmailUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val verifySuccess: Boolean = false
)
