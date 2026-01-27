package com.kuit.afternote.feature.onboarding.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.feature.auth.domain.usecase.VerifyEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 이메일 인증번호 확인 ViewModel.
 *
 * VerifyEmailUseCase를 통해 인증번호를 확인하고, 성공 시 verifySuccess를 설정한다.
 */
@HiltViewModel
class VerifyEmailViewModel @Inject constructor(
    private val verifyEmailUseCase: VerifyEmailUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(VerifyEmailUiState())
    val uiState: StateFlow<VerifyEmailUiState> = _uiState.asStateFlow()

    /**
     * 이메일 인증번호 확인.
     *
     * 성공 시 verifySuccess=true, 실패 시 errorMessage 설정.
     */
    fun verifyEmail(email: String, certificateCode: String) {
        android.util.Log.d("VerifyEmailViewModel", "verifyEmail 호출: email=$email, code=$certificateCode")
        if (email.isBlank() || certificateCode.isBlank()) {
            android.util.Log.w("VerifyEmailViewModel", "이메일 또는 인증번호가 비어있음")
            _uiState.update { it.copy(errorMessage = "이메일과 인증번호를 입력하세요.") }
            return
        }
        viewModelScope.launch {
            android.util.Log.d("VerifyEmailViewModel", "인증번호 검증 시작")
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            verifyEmailUseCase(email, certificateCode)
                .onSuccess {
                    android.util.Log.d("VerifyEmailViewModel", "인증번호 검증 성공: verifySuccess=true")
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = null, verifySuccess = true)
                    }
                }
                .onFailure { e ->
                    android.util.Log.e("VerifyEmailViewModel", "인증번호 검증 실패: ${e.message}", e)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.message ?: "인증번호 확인에 실패했습니다."
                        )
                    }
                }
        }
    }

    /**
     * verifySuccess 소비 후 호출.
     */
    fun clearVerifySuccess() {
        _uiState.update { it.copy(verifySuccess = false) }
    }

    /**
     * 에러 메시지 초기화.
     */
    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
