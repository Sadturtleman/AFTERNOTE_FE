package com.kuit.afternote.feature.onboarding.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.feature.auth.domain.usecase.SendEmailCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 이메일 인증번호 발송 ViewModel.
 *
 * SendEmailCodeUseCase를 통해 인증번호를 발송하고, 성공 시 sendSuccess를 설정한다.
 */
@HiltViewModel
class SendEmailCodeViewModel @Inject constructor(
    private val sendEmailCodeUseCase: SendEmailCodeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SendEmailCodeUiState())
    val uiState: StateFlow<SendEmailCodeUiState> = _uiState.asStateFlow()

    /**
     * 이메일 인증번호 발송.
     *
     * 성공 시 sendSuccess=true, 실패 시 errorMessage 설정.
     */
    fun sendEmailCode(email: String) {
        if (email.isBlank()) {
            _uiState.update { it.copy(errorMessage = "이메일을 입력하세요.") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            sendEmailCodeUseCase(email)
                .onSuccess {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = null, sendSuccess = true)
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
     * sendSuccess 소비 후 호출.
     */
    fun clearSendSuccess() {
        _uiState.update { it.copy(sendSuccess = false) }
    }

    /**
     * 에러 메시지 초기화.
     */
    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
