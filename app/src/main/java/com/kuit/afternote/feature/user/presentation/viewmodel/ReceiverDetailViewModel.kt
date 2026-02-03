package com.kuit.afternote.feature.user.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
        private val getReceiverDetailUseCase: GetReceiverDetailUseCase
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(ReceiverDetailUiState())
        val uiState: StateFlow<ReceiverDetailUiState> = _uiState.asStateFlow()

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
                                errorMessage = e.message ?: "수신인 상세 조회에 실패했습니다."
                            )
                        }
                    }
            }
        }

        fun clearError() {
            _uiState.update { it.copy(errorMessage = null) }
        }
    }
