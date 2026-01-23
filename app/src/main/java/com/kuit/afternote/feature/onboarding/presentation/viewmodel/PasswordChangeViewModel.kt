package com.kuit.afternote.feature.onboarding.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.feature.auth.domain.usecase.PasswordChangeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 비밀번호 변경 ViewModel.
 *
 * PasswordChangeUseCase를 통해 비밀번호를 변경하고, 성공 시 passwordChangeSuccess를 설정한다.
 */
@HiltViewModel
class PasswordChangeViewModel @Inject constructor(
    private val passwordChangeUseCase: PasswordChangeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PasswordChangeUiState())
    val uiState: StateFlow<PasswordChangeUiState> = _uiState.asStateFlow()

    /**
     * 비밀번호 변경 시도.
     *
     * 성공 시 passwordChangeSuccess=true, 실패 시 errorMessage 설정.
     */
    fun changePassword(currentPassword: String, newPassword: String) {
        when {
            currentPassword.isBlank() -> {
                _uiState.update { it.copy(errorMessage = "현재 비밀번호를 입력하세요.") }
            }
            newPassword.isBlank() -> {
                _uiState.update { it.copy(errorMessage = "새 비밀번호를 입력하세요.") }
            }
            else -> {
                runPasswordChange(currentPassword, newPassword)
            }
        }
    }

    private fun runPasswordChange(currentPassword: String, newPassword: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            passwordChangeUseCase(currentPassword, newPassword)
                .onSuccess {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = null, passwordChangeSuccess = true)
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.message ?: "비밀번호 변경에 실패했습니다."
                        )
                    }
                }
        }
    }

    /**
     * passwordChangeSuccess 소비 후 호출 (네비게이션 후).
     */
    fun clearPasswordChangeSuccess() {
        _uiState.update { it.copy(passwordChangeSuccess = false) }
    }

    /**
     * 에러 메시지 초기화.
     */
    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
