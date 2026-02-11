package com.kuit.afternote.feature.receiver.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.feature.receiver.domain.usecase.GetReceivedTimeLettersUseCase
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
 * 목록 API로 조회한 결과에서 [timeLetterId]에 해당하는 타임레터를 표시합니다.
 */
@HiltViewModel
class ReceiverTimeLetterDetailViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val getReceivedTimeLettersUseCase: GetReceivedTimeLettersUseCase
    ) : ViewModel() {

    private val _uiState = MutableStateFlow(ReceiverTimeLetterDetailUiState())
    val uiState: StateFlow<ReceiverTimeLetterDetailUiState> = _uiState.asStateFlow()

    init {
        val receiverId = savedStateHandle.get<String>("receiverId")?.toLongOrNull()
        val timeLetterId = savedStateHandle.get<String>("timeLetterId")?.toLongOrNull()
        if (receiverId != null && timeLetterId != null) loadLetter(receiverId, timeLetterId)
    }

    private fun loadLetter(receiverId: Long, timeLetterId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            getReceivedTimeLettersUseCase(receiverId = receiverId)
                .onSuccess { list ->
                    val letter = list.find { it.timeLetterId == timeLetterId }
                    _uiState.update {
                        it.copy(
                            letter = letter,
                            isLoading = false,
                            errorMessage = if (letter == null) "해당 타임레터를 찾을 수 없습니다." else null
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
