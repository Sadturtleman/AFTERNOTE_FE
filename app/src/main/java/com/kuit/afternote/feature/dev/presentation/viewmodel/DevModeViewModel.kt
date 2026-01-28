package com.kuit.afternote.feature.dev.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.data.local.TokenManager
import com.kuit.afternote.feature.auth.domain.usecase.LoginUseCase
import com.kuit.afternote.feature.auth.domain.usecase.LogoutUseCase
import com.kuit.afternote.feature.auth.domain.usecase.PasswordChangeUseCase
import com.kuit.afternote.feature.auth.domain.usecase.SignUpUseCase
import com.kuit.afternote.feature.dev.domain.LocalPropertiesManager
import com.kuit.afternote.feature.dev.domain.TestAccountManager
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
class DevModeViewModel
    @Inject
    constructor(
        private val tokenManager: TokenManager,
        private val loginUseCase: LoginUseCase,
        private val logoutUseCase: LogoutUseCase,
        private val passwordChangeUseCase: PasswordChangeUseCase,
        private val signUpUseCase: SignUpUseCase,
        private val testAccountManager: TestAccountManager,
        private val localPropertiesManager: LocalPropertiesManager
    ) : ViewModel() {
        private val isLoadingFlow = MutableStateFlow(false)
        private val messageFlow = MutableStateFlow<String?>(null)

        /**
         * UI 상태 Flow.
         */
        val uiState: StateFlow<DevModeUiState> = combine(
            tokenManager.isLoggedInFlow,
            tokenManager.userEmailFlow,
            isLoadingFlow,
            messageFlow
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
        fun quickLogin(
            email: String,
            password: String
        ) {
            Log.d(TAG, "quickLogin: email=$email")
            viewModelScope.launch {
                isLoadingFlow.value = true
                messageFlow.value = null

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
                            // Cycle Password 전략: 로그인 성공 시 비밀번호 저장
                            testAccountManager.updateStoredPassword(password)
                            Log.d(TAG, "Password saved to TestAccountManager")
                            messageFlow.value = "로그인 성공"
                        } else {
                            Log.w(TAG, "Tokens are empty")
                            messageFlow.value = "토큰이 없습니다"
                        }
                    }.onFailure { e ->
                        Log.e(TAG, "quickLogin FAILED", e)
                        messageFlow.value = "로그인 실패: ${e.message}"
                    }

                isLoadingFlow.value = false
            }
        }

        /**
         * 로그아웃.
         */
        fun logout() {
            Log.d(TAG, "logout called")
            viewModelScope.launch {
                isLoadingFlow.value = true
                messageFlow.value = null

                val refreshToken = tokenManager.getRefreshToken()
                Log.d(TAG, "refreshToken: ${refreshToken?.take(n = 20)}...")

                if (!refreshToken.isNullOrBlank()) {
                    logoutUseCase(refreshToken)
                }

                tokenManager.clearTokens()
                Log.d(TAG, "Tokens cleared")
                messageFlow.value = "로그아웃 완료"
                isLoadingFlow.value = false
            }
        }

        /**
         * 개발 모드 전용: Cycle Password (비밀번호 순환).
         *
         * 저장된 현재 비밀번호를 사용하여 실제 change-password API를 호출합니다.
         * 성공 시 새로운 비밀번호를 저장하여 다음 변경 시 사용할 수 있도록 합니다.
         *
         * @param newPassword 새로운 비밀번호
         * @param currentPassword 현재 비밀번호 (선택적, 제공되지 않으면 저장된 값 또는 BuildConfig.TEST_PASSWORD 사용)
         */
        fun cyclePassword(
            newPassword: String,
            currentPassword: String? = null
        ) {
            Log.d(TAG, "cyclePassword: newPassword length=${newPassword.length}")
            viewModelScope.launch {
                isLoadingFlow.value = true
                messageFlow.value = null

                // 현재 비밀번호 결정: 수동 입력 > 저장된 값 > BuildConfig.TEST_PASSWORD
                val actualCurrentPassword = currentPassword?.takeIf { it.isNotBlank() }
                    ?: testAccountManager.getCurrentPassword()

                Log.d(TAG, "cyclePassword: using currentPassword, length=${actualCurrentPassword.length}")

                if (actualCurrentPassword.isBlank()) {
                    Log.e(TAG, "cyclePassword: currentPassword is blank")
                    messageFlow.value = "현재 비밀번호가 필요합니다. 다이얼로그에서 현재 비밀번호를 입력하세요."
                    isLoadingFlow.value = false
                    return@launch
                }

                // 실제 change-password API 호출
                passwordChangeUseCase(actualCurrentPassword, newPassword)
                    .onSuccess {
                        Log.d(TAG, "cyclePassword SUCCESS")
                        // 성공 시 새로운 비밀번호 저장
                        testAccountManager.updateStoredPassword(newPassword)
                        // local.properties 업데이트 시도 (Debug 빌드에서만 실제 업데이트)
                        localPropertiesManager.updateTestPassword(newPassword)
                        Log.d(TAG, "cyclePassword: new password saved")
                        messageFlow.value = "비밀번호가 변경되었습니다: $newPassword"
                    }.onFailure { e ->
                        Log.e(TAG, "cyclePassword FAILED", e)
                        messageFlow.value = "비밀번호 변경 실패: ${e.message}"
                    }

                isLoadingFlow.value = false
            }
        }

        /**
         * 메시지 초기화.
         */
        fun clearMessage() {
            messageFlow.value = null
        }

        /**
         * 개발 모드 전용: Quick Test Account 생성 (빠른 테스트 계정 생성).
         *
         * 비밀번호를 잊었거나 테스트용 새 계정이 필요할 때 사용합니다.
         * 랜덤 이메일로 계정을 생성하고, 기본 비밀번호로 자동 로그인합니다.
         * 성공 시 비밀번호를 저장하여 Cycle Password 기능을 사용할 수 있도록 합니다.
         *
         * 참고: 이메일 인증을 건너뛰고 바로 회원가입 API를 호출합니다.
         * (회원가입 API는 이메일 인증을 필수로 요구하지 않음)
         */
        fun createQuickTestAccount() {
            Log.d(TAG, "createQuickTestAccount: creating new test account")
            viewModelScope.launch {
                isLoadingFlow.value = true
                messageFlow.value = null

                // 랜덤 이메일 생성 (타임스탬프 기반)
                val randomId = System.currentTimeMillis() % 1000000
                val email = "test_$randomId@afternote.dev"
                val defaultPassword = "TestPassword123!"
                val name = "Test User $randomId"

                Log.d(TAG, "createQuickTestAccount: email=$email, password=$defaultPassword")

                // 회원가입 (이메일 인증 없이 바로 진행)
                signUpUseCase(email, defaultPassword, name, null)
                    .onSuccess { signUpResult ->
                        Log.d(TAG, "createQuickTestAccount: signUp SUCCESS, userId=${signUpResult.userId}")

                        // 자동 로그인
                        loginUseCase(email, defaultPassword)
                            .onSuccess { loginResult ->
                                Log.d(TAG, "createQuickTestAccount: login SUCCESS")
                                val accessToken = loginResult.accessToken
                                val refreshToken = loginResult.refreshToken

                                if (!accessToken.isNullOrEmpty() && !refreshToken.isNullOrEmpty()) {
                                    tokenManager.saveTokens(
                                        accessToken = accessToken,
                                        refreshToken = refreshToken,
                                        email = email
                                    )
                                    Log.d(TAG, "Tokens saved to TokenManager")

                                    // 비밀번호 저장 (Cycle Password를 위해)
                                    testAccountManager.updateStoredPassword(defaultPassword)
                                    Log.d(TAG, "Password saved to TestAccountManager")

                                    messageFlow.value = "새 테스트 계정 생성 완료: $email (비밀번호: $defaultPassword)"
                                } else {
                                    Log.w(TAG, "Tokens are empty")
                                    messageFlow.value = "토큰이 없습니다"
                                }
                            }.onFailure { e ->
                                Log.e(TAG, "createQuickTestAccount: login FAILED", e)
                                messageFlow.value = "계정 생성 후 로그인 실패: ${e.message}"
                            }
                    }.onFailure { e ->
                        Log.e(TAG, "createQuickTestAccount: signUp FAILED", e)
                        messageFlow.value = "계정 생성 실패: ${e.message}"
                    }

                isLoadingFlow.value = false
            }
        }

        companion object {
            private const val TAG = "DevModeViewModel"
        }
    }
