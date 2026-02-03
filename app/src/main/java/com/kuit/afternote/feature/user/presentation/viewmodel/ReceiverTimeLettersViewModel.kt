package com.kuit.afternote.feature.user.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.feature.user.domain.usecase.GetReceiverTimeLettersUseCase
import com.kuit.afternote.feature.user.presentation.uimodel.ReceiverTimeLetterItemUi
import com.kuit.afternote.feature.user.presentation.uimodel.ReceiverTimeLettersUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 수신인별 타임레터 목록 화면 ViewModel.
 * GET /users/receivers/{receiverId}/time-letters
 */
@HiltViewModel
class ReceiverTimeLettersViewModel
    @Inject
    constructor(
        private val getReceiverTimeLettersUseCase: GetReceiverTimeLettersUseCase
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(ReceiverTimeLettersUiState())
        val uiState: StateFlow<ReceiverTimeLettersUiState> = _uiState.asStateFlow()

        fun loadTimeLetters(receiverId: Long) {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                getReceiverTimeLettersUseCase(receiverId = receiverId)
                    .onSuccess { list ->
                        _uiState.update {
                            it.copy(
                                items = list.map { item ->
                                    ReceiverTimeLetterItemUi(
                                        timeLetterId = item.timeLetterId,
                                        receiverName = item.receiverName,
                                        sendAt = item.sendAt,
                                        title = item.title,
                                        content = item.content
                                    )
                                },
                                isLoading = false,
                                errorMessage = null
                            )
                        }
                    }
                    .onFailure { e ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = e.message ?: "타임레터 목록 조회에 실패했습니다."
                            )
                        }
                    }
            }
        }

        fun clearError() {
            _uiState.update { it.copy(errorMessage = null) }
        }
    }
