package com.kuit.afternote.feature.user.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.feature.user.domain.usecase.GetReceiversUseCase
import com.kuit.afternote.feature.user.domain.usecase.GetUserIdUseCase
import com.kuit.afternote.feature.user.presentation.uimodel.ReceiverListItemUi
import com.kuit.afternote.feature.user.presentation.uimodel.ReceiverListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 수신인 목록 화면 ViewModel.
 * GET /users/receivers — 로그인한 사용자가 등록한 수신인 목록을 조회합니다.
 */
@HiltViewModel
class ReceiverListViewModel
    @Inject
    constructor(
        private val getReceiversUseCase: GetReceiversUseCase,
        private val getUserIdUseCase: GetUserIdUseCase
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(ReceiverListUiState())
        val uiState: StateFlow<ReceiverListUiState> = _uiState.asStateFlow()

        fun loadReceivers() {
            viewModelScope.launch {
                val userId = getUserIdUseCase()
                if (userId == null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "사용자 정보를 가져올 수 없습니다. 다시 로그인해주세요."
                        )
                    }
                    return@launch
                }
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                getReceiversUseCase(userId = userId)
                    .onSuccess { list ->
                        _uiState.update {
                            it.copy(
                                receivers = list.map { item ->
                                    ReceiverListItemUi(
                                        receiverId = item.receiverId,
                                        name = item.name,
                                        relation = item.relation
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
                                errorMessage = e.message ?: "수신인 목록 조회에 실패했습니다."
                            )
                        }
                    }
            }
        }

        fun clearError() {
            _uiState.update { it.copy(errorMessage = null) }
        }
    }
