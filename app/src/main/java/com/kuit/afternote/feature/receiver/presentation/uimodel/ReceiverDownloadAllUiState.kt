package com.kuit.afternote.feature.receiver.presentation.uimodel

/**
 * 모든 기록 내려받기 다이얼로그/화면 UI 상태.
 */
data class ReceiverDownloadAllUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val downloadSuccess: Boolean = false
)
