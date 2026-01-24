package com.kuit.afternote.feature.dev.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.data.local.TokenManager
import com.kuit.afternote.feature.auth.domain.usecase.LoginUseCase
import com.kuit.afternote.feature.auth.domain.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 개발자 모드 화면 UI 상태.
 */
data class DevModeUiState(
    val isLoggedIn: Boolean = false,
    val userEmail: String? = null,
    val isLoading: Boolean = false,
    val message: String? = null
)

/**
 * 개발자 모드 화면 ViewModel.
 *
 * 로그인 상태를 표시하고 빠른 로그인/로그아웃 기능을 제공합니다.
 */
@HiltViewModel
class DevModeViewModel @Inject constructor(
    private val tokenManager: TokenManager,
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    private val _message = MutableStateFlow<String?>(null)

    /**
     * UI 상태 Flow.
     */
    val uiState: StateFlow<DevModeUiState> = combine(
        tokenManager.isLoggedInFlow,
        tokenManager.userEmailFlow,
        _isLoading,
        _message
    ) { isLoggedIn, email, isLoading, message ->
        DevModeUiState(
            isLoggedIn = isLoggedIn,
            userEmail = email,
            isLoading = isLoading,
            message = message
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = DevModeUiState()
    )

    /**
     * 빠른 로그인 (테스트 계정).
     *
     * @param email 테스트 이메일
     * @param password 테스트 비밀번호
     */
    fun quickLogin(email: String, password: String) {
        Log.d(TAG, "quickLogin: email=$email")
        viewModelScope.launch {
            _isLoading.value = true
            _message.value = null

            loginUseCase(email, password)
                .onSuccess { result ->
                    Log.d(TAG, "quickLogin SUCCESS")
                    Log.d(TAG, "accessToken: ${result.accessToken?.take(n = 20)}...")
                    Log.d(TAG, "refreshToken: ${result.refreshToken?.take(n = 20)}...")

                    val accessToken = result.accessToken
                    val refreshToken = result.refreshToken
                    if (!accessToken.isNullOrEmpty() && !refreshToken.isNullOrEmpty()) {
                        tokenManager.saveTokens(
                            accessToken = accessToken,
                            refreshToken = refreshToken,
                            email = email
                        )
                        Log.d(TAG, "Tokens saved to TokenManager")
                        _message.value = "로그인 성공"
                    } else {
                        Log.w(TAG, "Tokens are empty")
                        _message.value = "토큰이 없습니다"
                    }
                }
                .onFailure { e ->
                    Log.e(TAG, "quickLogin FAILED", e)
                    _message.value = "로그인 실패: ${e.message}"
                }

            _isLoading.value = false
        }
    }

    /**
     * 로그아웃.
     */
    fun logout() {
        Log.d(TAG, "logout called")
        viewModelScope.launch {
            _isLoading.value = true
            _message.value = null

            val refreshToken = tokenManager.getRefreshToken()
            Log.d(TAG, "refreshToken: ${refreshToken?.take(n = 20)}...")

            if (!refreshToken.isNullOrBlank()) {
                logoutUseCase(refreshToken)
            }

            tokenManager.clearTokens()
            Log.d(TAG, "Tokens cleared")
            _message.value = "로그아웃 완료"
            _isLoading.value = false
        }
    }

    /**
     * 메시지 초기화.
     */
    fun clearMessage() {
        _message.value = null
    }

    companion object {
        private const val TAG = "DevModeViewModel"
    }
}
