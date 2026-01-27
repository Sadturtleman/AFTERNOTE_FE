package com.kuit.afternote.feature.onboarding.presentation.viewmodel

/**
 * 이메일 인증번호 발송 화면 UiState.
 */
data class SendEmailCodeUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val sendSuccess: Boolean = false
)
