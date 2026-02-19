package com.kuit.afternote.feature.user.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kuit.afternote.feature.dailyrecord.domain.usecase.SetMindRecordReceiverEnabledForAllUseCase
import com.kuit.afternote.feature.setting.presentation.navgraph.SettingRoute
import com.kuit.afternote.feature.user.domain.usecase.GetReceiverDetailUseCase
import com.kuit.afternote.feature.user.presentation.uimodel.ReceiverDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 수신인 상세 화면 ViewModel.
 * GET /users/receivers/{receiverId}
 */
@HiltViewModel
class ReceiverDetailViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val getReceiverDetailUseCase: GetReceiverDetailUseCase,
        private val setMindRecordReceiverEnabledForAllUseCase: SetMindRecordReceiverEnabledForAllUseCase
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(ReceiverDetailUiState())
        val uiState: StateFlow<ReceiverDetailUiState> = _uiState.asStateFlow()

        private val receiverId: Long? = savedStateHandle.toRoute<SettingRoute.ReceiverDetailRoute>()
            .receiverId.toLongOrNull()

        init {
            if (receiverId == null) {
                _uiState.update {
                    it.copy(name = "수신인", relation = "")
                }
            }
        }

        fun loadReceiverDetail(receiverId: Long) {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                getReceiverDetailUseCase(receiverId = receiverId)
                    .onSuccess { detail ->
                        _uiState.update {
                            it.copy(
                                receiverId = detail.receiverId,
                                name = detail.name,
                                relation = detail.relation,
                                phone = detail.phone,
                                email = detail.email,
                                dailyQuestionCount = detail.dailyQuestionCount,
                                timeLetterCount = detail.timeLetterCount,
                                afterNoteCount = detail.afterNoteCount,
                                isLoading = false,
                                errorMessage = null
                            )
                        }
                    }
                    .onFailure { e ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = e.message ?: "수신인 상세 조회에 실패했습니다.",
                                name = if (it.name.isEmpty()) "수신인" else it.name,
                                relation = it.relation
                            )
                        }
                    }
            }
        }

        fun clearError() {
            _uiState.update { it.copy(errorMessage = null) }
        }

        /**
         * "나의 모든 기록" 전달 허용 토글.
         * PATCH mind-records/{recordId}/receivers/{receiverId} 를 모든 마음의 기록에 대해 호출합니다.
         */
        fun setMindRecordDeliveryEnabled(receiverId: Long, enabled: Boolean) {
            viewModelScope.launch {
                val previous = _uiState.value.mindRecordDeliveryEnabled
                _uiState.update { it.copy(mindRecordDeliveryEnabled = enabled) }
                runCatching {
                    setMindRecordReceiverEnabledForAllUseCase(
                        receiverId = receiverId,
                        enabled = enabled
                    )
                }.onFailure { e ->
                    _uiState.update {
                        it.copy(
                            mindRecordDeliveryEnabled = previous,
                            errorMessage = e.message ?: "전달 설정 변경에 실패했습니다."
                        )
                    }
                }
            }
        }
    }
