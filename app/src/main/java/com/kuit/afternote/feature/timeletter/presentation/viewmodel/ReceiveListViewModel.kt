package com.kuit.afternote.feature.timeletter.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.feature.timeletter.presentation.mapper.toTimeLetterReceivers
import com.kuit.afternote.feature.timeletter.presentation.uimodel.ReceiveListUiState
import com.kuit.afternote.feature.user.domain.usecase.GetReceiversUseCase
import com.kuit.afternote.feature.user.domain.usecase.GetUserIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 타임레터 수신자 목록 화면 ViewModel.
 * GET /users/receivers (설정 > 수신자 목록과 동일 소스)를 사용합니다.
 */
@HiltViewModel
class ReceiveListViewModel
    @Inject
    constructor(
        private val getReceiversUseCase: GetReceiversUseCase,
        private val getUserIdUseCase: GetUserIdUseCase
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(ReceiveListUiState())
        val uiState: StateFlow<ReceiveListUiState> = _uiState.asStateFlow()

        /**
         * 수신자 목록 로드 (설정에서 등록한 수신자와 동일 목록)
         */
        fun loadReceivers() {
            viewModelScope.launch {
                val userId = getUserIdUseCase()
                if (userId == null) {
                    _uiState.update { it.copy(receivers = emptyList(), isLoading = false) }
                    return@launch
                }
                _uiState.update { it.copy(isLoading = true) }
                getReceiversUseCase(userId = userId)
                    .onSuccess { list ->
                        _uiState.update {
                            it.copy(
                                receivers = list.toTimeLetterReceivers(),
                                isLoading = false
                            )
                        }
                    }
                    .onFailure {
                        _uiState.update { it.copy(receivers = emptyList(), isLoading = false) }
                    }
            }
        }
    }
