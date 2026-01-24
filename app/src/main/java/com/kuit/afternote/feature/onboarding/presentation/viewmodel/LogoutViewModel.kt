package com.kuit.afternote.feature.onboarding.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.data.local.TokenManager
import com.kuit.afternote.feature.auth.domain.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 로그아웃 ViewModel.
 *
 * LogoutUseCase를 통해 로그아웃하고, 성공 시 토큰을 삭제하고 logoutSuccess를 설정한다.
 */
@HiltViewModel
class LogoutViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(LogoutUiState())
    val uiState: StateFlow<LogoutUiState> = _uiState.asStateFlow()

    /**
     * 로그아웃 시도 (저장된 토큰 사용).
     *
     * 성공 시 토큰 삭제 후 logoutSuccess=true, 실패 시 errorMessage 설정.
     */
    fun logout() {
        viewModelScope.launch {
            val refreshToken = tokenManager.getRefreshToken()
            if (refreshToken.isNullOrBlank()) {
                // 토큰이 없으면 로컬에서만 로그아웃 처리
                tokenManager.clearTokens()
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = null, logoutSuccess = true)
                }
                return@launch
            }

            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            logoutUseCase(refreshToken)
                .onSuccess {
                    // 토큰 삭제
                    tokenManager.clearTokens()
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = null, logoutSuccess = true)
                    }
                }
                .onFailure { e ->
                    // 실패해도 로컬 토큰은 삭제
                    tokenManager.clearTokens()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.message ?: "로그아웃에 실패했습니다.",
                            logoutSuccess = true
                        )
                    }
                }
        }
    }

    /**
     * 로그아웃 시도 (리프레시 토큰 직접 전달).
     *
     * @deprecated 저장된 토큰을 사용하는 logout()을 사용하세요.
     */
    @Deprecated("Use logout() without parameter", ReplaceWith("logout()"))
    fun logout(refreshToken: String) {
        if (refreshToken.isBlank()) {
            _uiState.update { it.copy(errorMessage = "리프레시 토큰을 입력하세요.") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            logoutUseCase(refreshToken)
                .onSuccess {
                    tokenManager.clearTokens()
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = null, logoutSuccess = true)
                    }
                }
                .onFailure { e ->
                    tokenManager.clearTokens()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.message ?: "로그아웃에 실패했습니다.",
                            logoutSuccess = true
                        )
                    }
                }
        }
    }

    /**
     * logoutSuccess 소비 후 호출 (네비게이션 후).
     */
    fun clearLogoutSuccess() {
        _uiState.update { it.copy(logoutSuccess = false) }
    }

    /**
     * 에러 메시지 초기화.
     */
    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
