package com.kuit.afternote.feature.receiver.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.feature.receiver.domain.usecase.GetReceivedAfterNotesUseCase
import com.kuit.afternote.feature.receiver.presentation.uimodel.ReceivedAfternoteListItemUi
import com.kuit.afternote.feature.receiver.presentation.uimodel.ReceiverAfternotesListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 수신인별 애프터노트 목록 화면 ViewModel (설정 플로우).
 *
 * GET /api/received/{receiverId}/after-notes API로 전달된 애프터노트 목록을 조회합니다.
 */
@HiltViewModel
class ReceiverAfternotesListViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val getReceivedAfterNotesUseCase: GetReceivedAfterNotesUseCase
    ) : ViewModel() {

        private val _uiState = MutableStateFlow(ReceiverAfternotesListUiState())
        val uiState: StateFlow<ReceiverAfternotesListUiState> = _uiState.asStateFlow()

        init {
            val receiverId = savedStateHandle.get<String>("receiverId")?.toLongOrNull()
            if (receiverId != null) loadAfterNotes(receiverId)
        }

        fun loadAfterNotes(receiverId: Long) {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                getReceivedAfterNotesUseCase(receiverId = receiverId)
                    .onSuccess { list ->
                        _uiState.update {
                            it.copy(
                                items = list.map { item ->
                                    ReceivedAfternoteListItemUi(
                                        sourceType = item.sourceType.orEmpty(),
                                        lastUpdatedAt = item.lastUpdatedAt.orEmpty()
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
