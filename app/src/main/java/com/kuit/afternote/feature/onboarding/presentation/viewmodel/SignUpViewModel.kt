package com.kuit.afternote.feature.onboarding.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.feature.auth.domain.usecase.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 회원가입 ViewModel.
 *
 * SignUpUseCase를 통해 회원가입하고, 성공 시 signUpSuccess를 설정한다.
 */
@HiltViewModel
class SignUpViewModel
    @Inject
    constructor(
        private val signUpUseCase: SignUpUseCase
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(SignUpUiState())
        val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

        /**
         * 회원가입 시도.
         *
         * 성공 시 signUpSuccess=true, 실패 시 errorMessage 설정.
         *
         * @param profileUrl 프로필 이미지 URL, null 가능
         */
        fun signUp(
            email: String,
            password: String,
            name: String,
            profileUrl: String?
        ) {
            when {
                email.isBlank() -> _uiState.update { it.copy(errorMessage = "이메일을 입력하세요.") }
                password.isBlank() -> _uiState.update { it.copy(errorMessage = "비밀번호를 입력하세요.") }
                name.isBlank() -> _uiState.update { it.copy(errorMessage = "이름을 입력하세요.") }
                else -> runSignUp(email, password, name, profileUrl)
            }
        }

        private fun runSignUp(
            email: String,
            password: String,
            name: String,
            profileUrl: String?
        ) {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                signUpUseCase(email, password, name, profileUrl)
                    .onSuccess {
                        _uiState.update {
                            it.copy(isLoading = false, errorMessage = null, signUpSuccess = true)
                        }
                    }.onFailure { e ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = e.message ?: "회원가입에 실패했습니다."
                            )
                        }
                    }
            }
        }

        /**
         * signUpSuccess 소비 후 호출.
         */
        fun clearSignUpSuccess() {
            _uiState.update { it.copy(signUpSuccess = false) }
        }

        /**
         * 에러 메시지 초기화.
         */
        fun clearError() {
            _uiState.update { it.copy(errorMessage = null) }
        }
    }
