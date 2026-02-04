package com.kuit.afternote.feature.onboarding.presentation.viewmodel

/**
 * 회원가입 화면 UiState.
 */
data class SignUpUiState(
    val pickedProfileImageUri: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val signUpSuccess: Boolean = false
)
