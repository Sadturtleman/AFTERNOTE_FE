package com.kuit.afternote.feature.onboarding.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.feature.auth.domain.usecase.ReissueUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 토큰 재발급 ViewModel.
 *
 * ReissueUseCase를 통해 토큰을 재발급하고, 성공 시 reissueSuccess를 설정한다.
 */
@HiltViewModel
class ReissueViewModel
    @Inject
    constructor(
        private val reissueUseCase: ReissueUseCase
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(ReissueUiState())
        val uiState: StateFlow<ReissueUiState> = _uiState.asStateFlow()

        /**
         * 토큰 재발급 시도.
         *
         * 성공 시 reissueSuccess=true, 실패 시 errorMessage 설정.
         */
        fun reissue(refreshToken: String) {
            if (refreshToken.isBlank()) {
                _uiState.update { it.copy(errorMessage = "리프레시 토큰을 입력하세요.") }
                return
            }
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                reissueUseCase(refreshToken)
                    .onSuccess {
                        _uiState.update {
                            it.copy(isLoading = false, errorMessage = null, reissueSuccess = true)
                        }
                    }.onFailure { e ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = e.message ?: "토큰 재발급에 실패했습니다."
                            )
                        }
                    }
            }
        }

        /**
         * reissueSuccess 소비 후 호출 (네비게이션 후).
         */
        fun clearReissueSuccess() {
            _uiState.update { it.copy(reissueSuccess = false) }
        }

        /**
         * 에러 메시지 초기화.
         */
        fun clearError() {
            _uiState.update { it.copy(errorMessage = null) }
        }
    }
