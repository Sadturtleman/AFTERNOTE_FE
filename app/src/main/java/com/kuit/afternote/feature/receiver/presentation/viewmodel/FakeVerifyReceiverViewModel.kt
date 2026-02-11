package com.kuit.afternote.feature.receiver.presentation.viewmodel

import com.kuit.afternote.feature.receiver.presentation.uimodel.VerifySelfStep
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Preview용 Fake ViewModel.
 */
class FakeVerifyReceiverViewModel : VerifyReceiverViewModelContract {

    private val _uiState = MutableStateFlow(VerifyReceiverUiState())
    override val uiState: StateFlow<VerifyReceiverUiState> = _uiState.asStateFlow()

    override fun setStep(newStep: VerifySelfStep) {
        _uiState.value = _uiState.value.copy(step = newStep)
    }

    override fun onStartAuth() {
        _uiState.value = _uiState.value.copy(step = VerifySelfStep.EMAIL_AUTH)
    }

    override fun onSendCode(email: String) {}

    override fun onVerifyCode(email: String, code: String) {}

    override fun clearVerifySuccess() {}
}
