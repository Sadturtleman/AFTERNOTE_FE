package com.kuit.afternote.feature.onboarding.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.data.local.TokenManager
import com.kuit.afternote.data.remote.ApiException
import com.kuit.afternote.feature.auth.domain.usecase.KakaoLoginUseCase
import com.kuit.afternote.feature.auth.domain.usecase.LoginUseCase
import com.kuit.afternote.feature.dev.domain.TestAccountManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.io.IOException
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
                        Log.e(TAG, "Login failed", e)
                        val message = mapLoginErrorToMessage(e)
                        _uiState.update {
                            it.copy(isLoading = false, errorMessage = message)
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
                        Log.e(TAG, "Kakao login failed", e)
                        val message = mapLoginErrorToMessage(e)
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = message.ifBlank { "카카오 로그인에 실패했습니다." }
                            )
                        }
                    }
            }
        }

        /**
         * 로그인/카카오 로그인 실패 시 사용자에게 보여줄 메시지로 변환.
         * 빈 문자열이 되지 않도록 항상 기본 문구를 반환한다.
         */
        private fun mapLoginErrorToMessage(e: Throwable): String {
            val raw = when (e) {
                is ApiException -> e.message.ifBlank { null }
                is HttpException -> parseLoginHttpError(e)
                is IOException -> "네트워크 연결을 확인해주세요."
                else -> e.message?.takeIf { it.isNotBlank() }
            }
            return raw?.takeIf { it.isNotBlank() } ?: "로그인에 실패했습니다."
        }

        private fun parseLoginHttpError(e: HttpException): String? =
            try {
                val errorBody = e.response()?.errorBody()?.string()
                if (!errorBody.isNullOrBlank()) {
                    val parsed = json.decodeFromString<LoginErrorResponse>(errorBody)
                    parsed.message?.takeIf { it.isNotBlank() } ?: mapLoginHttpCodeToMessage(e.code())
                } else {
                    mapLoginHttpCodeToMessage(e.code())
                }
            } catch (parseEx: Exception) {
                Log.e(TAG, "Failed to parse login error body", parseEx)
                mapLoginHttpCodeToMessage(e.code())
            }

        private fun mapLoginHttpCodeToMessage(code: Int): String =
            when (code) {
                400,
                401 -> "이메일 또는 비밀번호가 올바르지 않습니다."
                404 -> "등록되지 않은 이메일입니다."
                500, 502, 503 -> "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요."
                else -> "로그인에 실패했습니다. (오류 코드: $code)"
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

        companion object {
            private const val TAG = "LoginViewModel"
            private val json = Json {
                ignoreUnknownKeys = true
                isLenient = true
            }
        }
    }

/** Server error response body for login/auth failures. */
@Serializable
private data class LoginErrorResponse(
    val status: Int? = null,
    val code: Int? = null,
    val message: String? = null
)
