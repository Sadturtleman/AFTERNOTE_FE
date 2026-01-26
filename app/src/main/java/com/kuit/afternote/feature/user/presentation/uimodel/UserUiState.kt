package com.kuit.afternote.feature.user.presentation.uimodel

/**
 * User UI 상태 모델.
 */

data class ProfileUiState(
    val name: String = "",
    val email: String = "",
    val phone: String? = null,
    val profileImageUrl: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val updateSuccess: Boolean = false
)

data class PushSettingsUiState(
    val timeLetter: Boolean = false,
    val mindRecord: Boolean = false,
    val afterNote: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val updateSuccess: Boolean = false
)
