package com.kuit.afternote.feature.onboarding.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.data.local.TokenManager
import com.kuit.afternote.feature.auth.domain.usecase.KakaoLoginUseCase
import com.kuit.afternote.feature.auth.domain.usecase.LoginUseCase
import com.kuit.afternote.feature.dev.domain.TestAccountManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 로그인 화면 ViewModel.
 *
 * LoginUseCase를 통해 로그인하고, 성공 시 토큰을 저장하고 loginSuccess를 설정한다.
 */
@HiltViewModel
class LoginViewModel
    @Inject
    constructor(
        private val loginUseCase: LoginUseCase,
        private val kakaoLoginUseCase: KakaoLoginUseCase,
        private val tokenManager: TokenManager,
        private val testAccountManager: TestAccountManager
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(LoginUiState())
        val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

        /**
         * 로그인 시도.
         *
         * 성공 시 토큰 저장 후 loginSuccess=true, 실패 시 errorMessage 설정.
         */
        fun login(
            email: String,
            password: String
        ) {
            if (email.isBlank() || password.isBlank()) {
                _uiState.update { it.copy(errorMessage = "이메일과 비밀번호를 입력하세요.") }
                return
            }
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                loginUseCase(email, password)
                    .onSuccess { result ->
                        // 토큰 저장
                        val accessToken = result.accessToken
                        val refreshToken = result.refreshToken
                        if (!accessToken.isNullOrEmpty() && !refreshToken.isNullOrEmpty()) {
                            tokenManager.saveTokens(
                                accessToken = accessToken,
                                refreshToken = refreshToken,
                                email = email
                            )
                            // Cycle Password strategy: Automatically save password on successful login
                            // This allows password recovery even if forgotten later
                            testAccountManager.updateStoredPassword(password)
                        }
                        _uiState.update {
                            it.copy(isLoading = false, errorMessage = null, loginSuccess = true)
                        }
                    }.onFailure { e ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = e.message ?: "로그인에 실패했습니다."
                            )
                        }
                    }
            }
        }

        /**
         * 카카오 로그인 시도.
         *
         * 성공 시 토큰 저장 후 loginSuccess=true, 실패 시 errorMessage 설정.
         */
        fun kakaoLogin(kakaoAccessToken: String) {
            if (kakaoAccessToken.isBlank()) {
                _uiState.update { it.copy(errorMessage = "카카오 로그인에 실패했습니다.") }
                return
            }

            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                kakaoLoginUseCase(kakaoAccessToken)
                    .onSuccess { result ->
                        val accessToken = result.accessToken
                        val refreshToken = result.refreshToken
                        if (!accessToken.isNullOrEmpty() && !refreshToken.isNullOrEmpty()) {
                            tokenManager.saveTokens(
                                accessToken = accessToken,
                                refreshToken = refreshToken
                            )
                        }
                        _uiState.update {
                            it.copy(isLoading = false, errorMessage = null, loginSuccess = true)
                        }
                    }.onFailure { e ->
                        Log.e("KakaoLogin", "Server /auth/kakao failed. type=${e::class.java.name}", e)
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = e.message ?: "카카오 로그인에 실패했습니다."
                            )
                        }
                    }
            }
        }

        /**
         * loginSuccess 소비 후 호출 (네비게이션 후).
         */
        fun clearLoginSuccess() {
            _uiState.update { it.copy(loginSuccess = false) }
        }

        /**
         * 에러 메시지 초기화.
         */
        fun clearError() {
            _uiState.update { it.copy(errorMessage = null) }
        }

        /**
         * 화면에서 발생한 에러 메시지를 표시합니다. (예: 소셜 로그인 실패 등)
         */
        fun setErrorMessage(message: String) {
            _uiState.update { it.copy(errorMessage = message) }
        }
    }
