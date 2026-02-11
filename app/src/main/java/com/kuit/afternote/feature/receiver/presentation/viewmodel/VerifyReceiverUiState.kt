package com.kuit.afternote.feature.receiver.presentation.viewmodel

import com.kuit.afternote.feature.receiver.presentation.uimodel.VerifySelfStep

/**
 * 수신자 이메일 인증 화면 UiState.
 */
data class VerifyReceiverUiState(
    val step: VerifySelfStep = VerifySelfStep.START,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val verifySuccess: Boolean = false
)
