package com.kuit.afternote.feature.receiver.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.feature.receiver.domain.usecase.GetTimeLetterDetailByAuthCodeUseCase
import com.kuit.afternote.feature.receiver.presentation.uimodel.ReceiverTimeLetterDetailUiState
import com.kuit.afternote.feature.receiverauth.session.ReceiverAuthSessionHolder
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
 * GET /api/receiver-auth/time-letters/{timeLetterReceiverId} (X-Auth-Code) API로 상세를 조회합니다.
 */
@HiltViewModel
class ReceiverTimeLetterDetailViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val receiverAuthSessionHolder: ReceiverAuthSessionHolder,
        private val getTimeLetterDetailByAuthCodeUseCase: GetTimeLetterDetailByAuthCodeUseCase
    ) : ViewModel() {

    private val _uiState = MutableStateFlow(ReceiverTimeLetterDetailUiState())
    val uiState: StateFlow<ReceiverTimeLetterDetailUiState> = _uiState.asStateFlow()

    init {
        val timeLetterReceiverId =
            savedStateHandle.get<String>("timeLetterReceiverId")?.toLongOrNull()
        val authCode = receiverAuthSessionHolder.getAuthCode()

        when {
            authCode == null || authCode.isBlank() -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "인증 정보가 없습니다."
                    )
                }
            }
            timeLetterReceiverId == null -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "타임레터 정보를 찾을 수 없습니다."
                    )
                }
            }
            else -> loadDetail(authCode = authCode, timeLetterReceiverId = timeLetterReceiverId)
        }
    }

    private fun loadDetail(authCode: String, timeLetterReceiverId: Long) {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            getTimeLetterDetailByAuthCodeUseCase(
                authCode = authCode,
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
                            errorMessage = e.message ?: "상세 조회에 실패했습니다."
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
