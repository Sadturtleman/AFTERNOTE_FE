package com.kuit.afternote.feature.receiver.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.feature.receiver.domain.usecase.SendReceiverEmailCodeUseCase
import com.kuit.afternote.feature.receiver.domain.usecase.VerifyReceiverEmailCodeUseCase
import com.kuit.afternote.feature.receiver.presentation.uimodel.VerifySelfStep
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 수신자 이메일 인증 화면 ViewModel.
 *
 * 이메일 발송/검증 UseCase를 호출하며, API 연동 전에는 No-op 구현으로 플로우만 진행.
 */
@HiltViewModel
class VerifyReceiverViewModel
    @Inject
    constructor(
        private val sendReceiverEmailCodeUseCase: SendReceiverEmailCodeUseCase,
        private val verifyReceiverEmailCodeUseCase: VerifyReceiverEmailCodeUseCase
    ) : ViewModel(), VerifyReceiverViewModelContract {

    private val _uiState = MutableStateFlow(VerifyReceiverUiState())
    override val uiState: StateFlow<VerifyReceiverUiState> = _uiState.asStateFlow()

    /**
     * 단계 변경 (뒤로가기 등).
     */
    override fun setStep(newStep: VerifySelfStep) {
        _uiState.update { it.copy(step = newStep) }
    }

    /**
     * 인증 시작하기: START → EMAIL_AUTH.
     */
    override fun onStartAuth() {
        _uiState.update { it.copy(step = VerifySelfStep.EMAIL_AUTH) }
    }

    /**
     * 다음 (이메일 단계): 발송 UseCase 호출 후 EMAIL_CODE로 전환.
     *
     * @param email 발송할 이메일 주소
     */
    override fun onSendCode(email: String) {
        if (email.isBlank()) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            sendReceiverEmailCodeUseCase(email)
                .onSuccess {
                    _uiState.update {
                        it.copy(step = VerifySelfStep.EMAIL_CODE, isLoading = false)
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.message ?: "인증번호 발송에 실패했습니다."
                        )
                    }
                }
        }
    }

    /**
     * 인증하기: 검증 UseCase 호출 후 성공 시 verifySuccess = true.
     *
     * @param email 이메일 주소
     * @param code 인증번호 (6자리)
     */
    override fun onVerifyCode(email: String, code: String) {
        if (code.length != 6) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            verifyReceiverEmailCodeUseCase(email, code)
                .onSuccess {
                    _uiState.update {
                        it.copy(isLoading = false, verifySuccess = true)
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.message ?: "인증에 실패했습니다."
                        )
                    }
                }
        }
    }

    /**
     * verifySuccess 소비 후 호출.
     */
    override fun clearVerifySuccess() {
        _uiState.update { it.copy(verifySuccess = false) }
    }

    /**
     * 에러 메시지 초기화.
     */
    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
