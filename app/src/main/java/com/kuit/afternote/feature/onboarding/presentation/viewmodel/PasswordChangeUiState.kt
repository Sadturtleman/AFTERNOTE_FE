package com.kuit.afternote.feature.onboarding.presentation.viewmodel

/**
 * 비밀번호 변경 화면 UiState.
 */
data class PasswordChangeUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val passwordChangeSuccess: Boolean = false,
    // Optimistic update 실패 시 rollback 필요
    val needsRollback: Boolean = false
)
