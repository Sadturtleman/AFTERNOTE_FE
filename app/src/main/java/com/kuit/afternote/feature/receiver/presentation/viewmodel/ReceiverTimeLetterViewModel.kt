package com.kuit.afternote.feature.receiver.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.feature.receiver.domain.usecase.GetReceivedTimeLettersUseCase
import com.kuit.afternote.feature.receiver.presentation.uimodel.ReceiverTimeLetterUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 수신자 앱 타임레터 목록 화면 ViewModel.
 *
 * GET /api/received/{receiverId}/time-letters API로 배달된 타임레터 목록을 조회합니다.
 */
@HiltViewModel
class ReceiverTimeLetterViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val getReceivedTimeLettersUseCase: GetReceivedTimeLettersUseCase
    ) : ViewModel() {

    private val _uiState = MutableStateFlow(ReceiverTimeLetterUiState())
    val uiState: StateFlow<ReceiverTimeLetterUiState> = _uiState.asStateFlow()

    init {
        val receiverId = savedStateHandle.get<String>("receiverId")?.toLongOrNull()
        if (receiverId != null) loadTimeLetters(receiverId)
    }

    /**
     * 타임레터 목록을 로드합니다.
     */
    fun loadTimeLetters(receiverId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            getReceivedTimeLettersUseCase(receiverId = receiverId)
                .onSuccess { data ->
                    _uiState.update {
                        it.copy(
                            timeLetters = data.items,
                            totalCount = data.totalCount,
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

    fun updateSelectedBottomNavItem(item: BottomNavItem) {
        _uiState.update { it.copy(selectedBottomNavItem = item) }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun sortByDateAscending() {
        _uiState.update { state ->
            // sendAt이 "2023. 11. 24" 같은 문자열 형식이므로 문자열 정렬로도 날짜순 정렬이 가능합니다.
            val sortedList = state.timeLetters.sortedBy { it.sendAt }
            state.copy(timeLetters = sortedList)
        }
    }

    fun sortByUnreadFirst() {
        _uiState.update { state ->
            val sortedList = state.timeLetters.sortedBy { letter ->
                // 예시: status가 "READ"이면 true(뒤로 감), 아니면 false(앞으로 옴)
                // 실제 사용하는 상태값(Enum 등)에 맞춰 "READ" 부분을 수정해주세요.
                letter.status == "READ"
            }
            state.copy(timeLetters = sortedList)
        }
    }
}
