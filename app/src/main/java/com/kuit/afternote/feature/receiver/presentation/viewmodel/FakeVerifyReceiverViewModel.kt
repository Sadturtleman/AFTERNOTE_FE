package com.kuit.afternote.feature.receiver.presentation.viewmodel

import com.kuit.afternote.feature.receiver.presentation.uimodel.VerifySelfStep
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Preview용 Fake ViewModel.
 *
 * @param initialState 초기 UiState (Preview에서 단계별 화면을 보여주기 위해 사용).
 */
class FakeVerifyReceiverViewModel(
    initialState: VerifyReceiverUiState = VerifyReceiverUiState()
) : VerifyReceiverViewModelContract {

    private val _uiState = MutableStateFlow(initialState)
    override val uiState: StateFlow<VerifyReceiverUiState> = _uiState.asStateFlow()

    override fun setStep(newStep: VerifySelfStep) {
        _uiState.value = _uiState.value.copy(step = newStep)
    }

    override fun onStartAuth() {
        _uiState.value = _uiState.value.copy(step = VerifySelfStep.EMAIL_AUTH)
    }

    override fun onSendCode(email: String) {
        // No-op: Fake for Preview only; no API call.
    }

    override fun onVerifyCode(email: String, code: String) {
        // No-op: Fake for Preview only; no API call.
    }

    override fun clearVerifySuccess() {
        // No-op: Fake for Preview only.
    }
}
