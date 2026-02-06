package com.kuit.afternote.feature.user.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.feature.user.domain.usecase.GetMyPushSettingsUseCase
import com.kuit.afternote.feature.user.domain.usecase.GetUserIdUseCase
import com.kuit.afternote.feature.user.domain.usecase.UpdateMyPushSettingsUseCase
import com.kuit.afternote.feature.user.presentation.uimodel.PushSettingsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 푸시 알림 설정 화면 ViewModel.
 */
@HiltViewModel
class PushSettingsViewModel
    @Inject
    constructor(
        private val getMyPushSettingsUseCase: GetMyPushSettingsUseCase,
        private val updateMyPushSettingsUseCase: UpdateMyPushSettingsUseCase,
        private val getUserIdUseCase: GetUserIdUseCase
    ) : ViewModel(), PushSettingsViewModelContract {
        private val _uiState = MutableStateFlow(PushSettingsUiState())
        override val uiState: StateFlow<PushSettingsUiState> = _uiState.asStateFlow()

        /**
         * 푸시 알림 설정 조회.
         * JWT 토큰에서 userId를 자동으로 추출합니다.
         */
        override fun loadPushSettings() {
            viewModelScope.launch {
                val userId = getUserIdUseCase()
                if (userId == null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "사용자 정보를 가져올 수 없습니다. 다시 로그인해주세요."
                        )
                    }
                    return@launch
                }

                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                getMyPushSettingsUseCase(userId = userId)
                    .onSuccess { settings ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                timeLetter = settings.timeLetter,
                                mindRecord = settings.mindRecord,
                                afterNote = settings.afterNote,
                                errorMessage = null
                            )
                        }
                    }.onFailure { e ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = e.message ?: "푸시 알림 설정 조회에 실패했습니다."
                            )
                        }
                    }
            }
        }

        /**
         * 푸시 알림 설정 수정.
         * JWT 토큰에서 userId를 자동으로 추출합니다.
         */
        override fun updatePushSettings(
            timeLetter: Boolean?,
            mindRecord: Boolean?,
            afterNote: Boolean?
        ) {
            viewModelScope.launch {
                val userId = getUserIdUseCase()
                if (userId == null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "사용자 정보를 가져올 수 없습니다. 다시 로그인해주세요.",
                            updateSuccess = false
                        )
                    }
                    return@launch
                }

                _uiState.update { it.copy(isLoading = true, errorMessage = null, updateSuccess = false) }
                updateMyPushSettingsUseCase(
                    userId = userId,
                    timeLetter = timeLetter,
                    mindRecord = mindRecord,
                    afterNote = afterNote
                ).onSuccess { settings ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            timeLetter = settings.timeLetter,
                            mindRecord = settings.mindRecord,
                            afterNote = settings.afterNote,
                            errorMessage = null,
                            updateSuccess = true
                        )
                    }
                }.onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.message ?: "푸시 알림 설정 수정에 실패했습니다.",
                            updateSuccess = false
                        )
                    }
                }
            }
        }

        /**
         * updateSuccess 소비 후 호출 (네비게이션 후).
         */
        override fun clearUpdateSuccess() {
            _uiState.update { it.copy(updateSuccess = false) }
        }

        /**
         * 에러 메시지 초기화.
         */
        override fun clearError() {
            _uiState.update { it.copy(errorMessage = null) }
        }
    }
