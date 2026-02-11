package com.kuit.afternote.feature.receiver.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.feature.receiver.domain.usecase.GetReceivedTimeLettersUseCase
import com.kuit.afternote.feature.receiver.presentation.uimodel.ReceivedTimeLetterListItemUi
import com.kuit.afternote.feature.receiver.presentation.uimodel.ReceiverTimeLettersListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 수신인별 타임레터 목록 화면 ViewModel (설정 플로우).
 *
 * GET /api/received/{receiverId}/time-letters API로 배달된 타임레터 목록을 조회합니다.
 */
@HiltViewModel
class ReceiverTimeLettersListViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val getReceivedTimeLettersUseCase: GetReceivedTimeLettersUseCase
    ) : ViewModel() {

        private val _uiState = MutableStateFlow(ReceiverTimeLettersListUiState())
        val uiState: StateFlow<ReceiverTimeLettersListUiState> = _uiState.asStateFlow()

        init {
            val receiverId = savedStateHandle.get<String>("receiverId")?.toLongOrNull()
            if (receiverId != null) loadTimeLetters(receiverId)
        }

        fun loadTimeLetters(receiverId: Long) {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                getReceivedTimeLettersUseCase(receiverId = receiverId)
                    .onSuccess { list ->
                        _uiState.update {
                            it.copy(
                                items = list.map { item ->
                                    ReceivedTimeLetterListItemUi(
                                        timeLetterId = item.timeLetterId,
                                        senderName = item.senderName.orEmpty(),
                                        sendAt = item.sendAt.orEmpty(),
                                        title = item.title.orEmpty(),
                                        content = item.content.orEmpty()
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
