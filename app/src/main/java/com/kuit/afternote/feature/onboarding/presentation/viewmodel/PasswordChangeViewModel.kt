package com.kuit.afternote.feature.onboarding.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.feature.auth.domain.usecase.PasswordChangeUseCase
import com.kuit.afternote.feature.dev.domain.LocalPropertiesManager
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
 * 비밀번호 변경 ViewModel.
 *
 * PasswordChangeUseCase를 통해 비밀번호를 변경하고, 성공 시 passwordChangeSuccess를 설정한다.
 */
@HiltViewModel
class PasswordChangeViewModel
    @Inject
    constructor(
        private val passwordChangeUseCase: PasswordChangeUseCase,
        private val localPropertiesManager: LocalPropertiesManager
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(PasswordChangeUiState())
        val uiState: StateFlow<PasswordChangeUiState> = _uiState.asStateFlow()

        // Race condition 방지: 진행 중 요청 식별자
        private var currentRequestId = 0

        private val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }

        /**
         * 비밀번호 변경 시도.
         *
         * 성공 시 passwordChangeSuccess=true, 실패 시 errorMessage 설정.
         */
        fun changePassword(
            currentPassword: String,
            newPassword: String
        ) {
            Log.d(TAG, "changePassword called")
            Log.d(TAG, "currentPassword length: ${currentPassword.length}")
            Log.d(TAG, "newPassword length: ${newPassword.length}")

            when {
                currentPassword.isBlank() -> {
                    Log.w(TAG, "Validation failed: currentPassword is blank")
                    _uiState.update { it.copy(errorMessage = "현재 비밀번호를 입력하세요.") }
                }
                newPassword.isBlank() -> {
                    Log.w(TAG, "Validation failed: newPassword is blank")
                    _uiState.update { it.copy(errorMessage = "새 비밀번호를 입력하세요.") }
                }
                else -> {
                    Log.d(TAG, "Validation passed, calling UseCase")
                    runPasswordChange(currentPassword, newPassword)
                }
            }
        }

        private fun runPasswordChange(
            currentPassword: String,
            newPassword: String
        ) {
            viewModelScope.launch {
                Log.d(TAG, "runPasswordChange started - Optimistic update")

                // Race condition 방지: 요청 ID 할당 및 이전 상태 스냅샷 저장
                val requestId = ++currentRequestId
                val previousState = _uiState.value

                // Optimistic update: 즉시 성공 상태로 설정하고 네비게이션
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = null,
                        passwordChangeSuccess = true,
                        needsRollback = false
                    )
                }

                // 백그라운드에서 실제 API 호출
                passwordChangeUseCase(currentPassword, newPassword)
                    .onSuccess {
                        // 최신 요청인지 확인 (레이스 컨디션 방지)
                        if (requestId == currentRequestId) {
                            Log.d(TAG, "Password change SUCCESS - API confirmed")
                            // local.properties 업데이트 시도 (Debug 빌드에서만 실제 업데이트)
                            localPropertiesManager.updateTestPassword(newPassword)
                            // 이미 성공 상태이므로 추가 업데이트 불필요
                        }
                    }.onFailure { e ->
                        // 최신 요청인지 확인 (레이스 컨디션 방지)
                        if (requestId == currentRequestId) {
                            Log.e(TAG, "Password change FAILED after optimistic update", e)
                            Log.e(TAG, "Error message: ${e.message}")
                            Log.e(TAG, "Error class: ${e::class.java.simpleName}")

                            // Rollback: 이전 상태로 복원
                            _uiState.update {
                                previousState.copy(
                                    isLoading = false,
                                    passwordChangeSuccess = false,
                                    needsRollback = true, // 화면으로 돌아가야 함
                                    errorMessage = mapErrorToUserMessage(e)
                                )
                            }
                        }
                    }
            }
        }

        /**
         * 예외를 사용자 친화적인 메시지로 변환합니다.
         */
        private fun mapErrorToUserMessage(e: Throwable): String =
            when (e) {
                is HttpException -> parseHttpError(e)
                is IOException -> "네트워크 연결을 확인해주세요."
                else -> e.message ?: "비밀번호 변경에 실패했습니다."
            }

        /**
         * HTTP 에러 응답을 파싱하여 서버 메시지를 추출합니다.
         */
        private fun parseHttpError(e: HttpException): String =
            try {
                val errorBody = e.response()?.errorBody()?.string()
                Log.d(TAG, "Error body: $errorBody")

                if (errorBody != null) {
                    val errorResponse = json.decodeFromString<ErrorResponse>(errorBody)
                    val serverMessage = errorResponse.message

                    // 서버 메시지가 있으면 한글로 변환, 없으면 상태 코드 기반 메시지
                    if (!serverMessage.isNullOrBlank()) {
                        mapServerMessageToKorean(serverMessage, e.code())
                    } else {
                        mapHttpCodeToMessage(e.code())
                    }
                } else {
                    mapHttpCodeToMessage(e.code())
                }
            } catch (parseException: Exception) {
                Log.e(TAG, "Failed to parse error body", parseException)
                mapHttpCodeToMessage(e.code())
            }

        /**
         * 서버 메시지를 사용자 친화적인 한글 메시지로 변환합니다.
         */
        private fun mapServerMessageToKorean(
            serverMessage: String,
            code: Int
        ): String {
            // 서버 메시지에서 키워드를 찾아 적절한 한글 메시지 반환
            return when {
                serverMessage.contains("format", ignoreCase = true) ||
                    serverMessage.contains("pattern", ignoreCase = true) ->
                    "비밀번호는 영문, 숫자, 특수문자를 포함한 8~20자여야 합니다."

                serverMessage.contains("current", ignoreCase = true) &&
                    serverMessage.contains("password", ignoreCase = true) ->
                    "현재 비밀번호가 일치하지 않습니다."

                serverMessage.contains("wrong", ignoreCase = true) ||
                    serverMessage.contains("incorrect", ignoreCase = true) ||
                    serverMessage.contains("invalid", ignoreCase = true) ->
                    "현재 비밀번호가 일치하지 않습니다."

                serverMessage.contains("same", ignoreCase = true) ->
                    "새 비밀번호가 현재 비밀번호와 동일합니다."

                serverMessage.contains("expired", ignoreCase = true) ||
                    serverMessage.contains("token", ignoreCase = true) ->
                    "로그인이 만료되었습니다. 다시 로그인해주세요."

                // 서버 메시지가 한글이면 그대로 사용
                serverMessage.any { it in '\uAC00'..'\uD7A3' } -> serverMessage

                // 기타: 상태 코드 기반 메시지
                else -> mapHttpCodeToMessage(code)
            }
        }

        /**
         * HTTP 상태 코드를 사용자 친화적인 메시지로 변환합니다.
         */
        private fun mapHttpCodeToMessage(code: Int): String =
            when (code) {
                400 -> "입력 정보를 확인해주세요."
                401 -> "인증이 만료되었습니다. 다시 로그인해주세요."
                403 -> "비밀번호 변경 권한이 없습니다."
                404 -> "사용자 정보를 찾을 수 없습니다."
                500, 502, 503 -> "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요."
                else -> "비밀번호 변경에 실패했습니다. (오류 코드: $code)"
            }

        /**
         * passwordChangeSuccess 소비 후 호출 (네비게이션 후).
         */
        fun clearPasswordChangeSuccess() {
            _uiState.update { it.copy(passwordChangeSuccess = false) }
        }

        /**
         * Rollback 상태 소비 후 호출 (화면으로 돌아온 후).
         */
        fun clearRollback() {
            _uiState.update { it.copy(needsRollback = false) }
        }

        /**
         * 에러 메시지 초기화.
         */
        fun clearError() {
            _uiState.update { it.copy(errorMessage = null) }
        }

        companion object {
            private const val TAG = "PasswordChangeVM"
        }
    }

/**
 * 서버 에러 응답 파싱용 데이터 클래스.
 */
@Serializable
private data class ErrorResponse(
    val status: Int? = null,
    val code: Int? = null,
    val message: String? = null
)
