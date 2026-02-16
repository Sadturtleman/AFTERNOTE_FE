package com.kuit.afternote.feature.receiver.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.feature.receiver.domain.usecase.GetReceivedTimeLetterDetailUseCase
import com.kuit.afternote.feature.receiver.presentation.uimodel.ReceiverTimeLetterDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 수신자 앱 타임레터 상세 화면 ViewModel.
 *
 * GET /api/received/{receiverId}/time-letters/{timeLetterReceiverId} API로 상세 조회 및 읽음 처리합니다.
 */
@HiltViewModel
class ReceiverTimeLetterDetailViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val getReceivedTimeLetterDetailUseCase: GetReceivedTimeLetterDetailUseCase
    ) : ViewModel() {

    private val _uiState = MutableStateFlow(ReceiverTimeLetterDetailUiState())
    val uiState: StateFlow<ReceiverTimeLetterDetailUiState> = _uiState.asStateFlow()

    init {
        val receiverId = savedStateHandle.get<String>("receiverId")?.toLongOrNull()
        val timeLetterReceiverId = savedStateHandle.get<String>("timeLetterReceiverId")?.toLongOrNull()
        if (receiverId != null && timeLetterReceiverId != null) {
            loadLetter(receiverId, timeLetterReceiverId)
        }
    }

    private fun loadLetter(receiverId: Long, timeLetterReceiverId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            getReceivedTimeLetterDetailUseCase(
                receiverId = receiverId,
                timeLetterReceiverId = timeLetterReceiverId
            )
                .onSuccess { letter ->
                    _uiState.update {
                        it.copy(
                            letter = letter,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.message ?: "타임레터 조회에 실패했습니다."
                        )
                    }
                }
        }
    }

    fun updateSelectedBottomNavItem(item: BottomNavItem) {
        _uiState.update { it.copy(selectedBottomNavItem = item) }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
