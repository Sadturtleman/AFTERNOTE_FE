package com.kuit.afternote.feature.receiver.presentation.viewmodel

import com.kuit.afternote.feature.receiver.presentation.uimodel.VerifySelfStep
import kotlinx.coroutines.flow.StateFlow

/**
 * 수신자 인증 화면 ViewModel 계약 (Preview용).
 */
interface VerifyReceiverViewModelContract {

    val uiState: StateFlow<VerifyReceiverUiState>

    fun setStep(newStep: VerifySelfStep)
    fun onStartAuth()
    fun onSendCode(email: String)
    fun onVerifyCode(email: String, code: String)
    fun clearVerifySuccess()
}
