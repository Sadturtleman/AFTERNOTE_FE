package com.kuit.afternote.feature.user.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kuit.afternote.feature.setting.presentation.navgraph.SettingRoute
import com.kuit.afternote.feature.user.domain.usecase.GetReceiverDetailUseCase
import com.kuit.afternote.feature.user.domain.usecase.GetUserIdUseCase
import com.kuit.afternote.feature.user.presentation.uimodel.ReceiverDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 수신인 상세 화면 ViewModel.
 * GET /users/receivers/{receiverId}
 */
@HiltViewModel
class ReceiverDetailViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val getReceiverDetailUseCase: GetReceiverDetailUseCase,
        private val getUserIdUseCase: GetUserIdUseCase
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(ReceiverDetailUiState())
        val uiState: StateFlow<ReceiverDetailUiState> = _uiState.asStateFlow()

        private val receiverId: Long? = savedStateHandle.toRoute<SettingRoute.ReceiverDetailRoute>()
            .receiverId.toLongOrNull()

        init {
            if (receiverId != null) {
                loadReceiverDetail(receiverId)
            } else {
                _uiState.update {
                    it.copy(name = "수신인", relation = "")
                }
            }
        }

        fun loadReceiverDetail(receiverId: Long) {
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
                getReceiverDetailUseCase(userId = userId, receiverId = receiverId)
                    .onSuccess { detail ->
                        _uiState.update {
                            it.copy(
                                receiverId = detail.receiverId,
                                name = detail.name,
                                relation = detail.relation,
                                phone = detail.phone,
                                email = detail.email,
                                dailyQuestionCount = detail.dailyQuestionCount,
                                timeLetterCount = detail.timeLetterCount,
                                afterNoteCount = detail.afterNoteCount,
                                isLoading = false,
                                errorMessage = null
                            )
                        }
                    }
                    .onFailure { e ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = e.message ?: "수신인 상세 조회에 실패했습니다.",
                                name = if (it.name.isEmpty()) "수신인" else it.name,
                                relation = it.relation
                            )
                        }
                    }
            }
        }

        fun clearError() {
            _uiState.update { it.copy(errorMessage = null) }
        }
    }
