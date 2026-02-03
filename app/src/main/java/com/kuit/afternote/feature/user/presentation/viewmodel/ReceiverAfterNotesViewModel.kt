package com.kuit.afternote.feature.user.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.feature.user.domain.usecase.GetReceiverAfterNotesUseCase
import com.kuit.afternote.feature.user.presentation.uimodel.ReceiverAfterNoteSourceItemUi
import com.kuit.afternote.feature.user.presentation.uimodel.ReceiverAfterNotesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 수신인별 애프터노트 목록 화면 ViewModel.
 * GET /users/receivers/{receiverId}/after-notes
 */
@HiltViewModel
class ReceiverAfterNotesViewModel
    @Inject
    constructor(
        private val getReceiverAfterNotesUseCase: GetReceiverAfterNotesUseCase
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(ReceiverAfterNotesUiState())
        val uiState: StateFlow<ReceiverAfterNotesUiState> = _uiState.asStateFlow()

        fun loadAfterNotes(receiverId: Long) {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                getReceiverAfterNotesUseCase(receiverId = receiverId)
                    .onSuccess { list ->
                        _uiState.update {
                            it.copy(
                                items = list.map { item ->
                                    ReceiverAfterNoteSourceItemUi(
                                        sourceType = item.sourceType,
                                        lastUpdatedAt = item.lastUpdatedAt
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
                                errorMessage = e.message ?: "애프터노트 목록 조회에 실패했습니다."
                            )
                        }
                    }
            }
        }

        fun clearError() {
            _uiState.update { it.copy(errorMessage = null) }
        }
    }
