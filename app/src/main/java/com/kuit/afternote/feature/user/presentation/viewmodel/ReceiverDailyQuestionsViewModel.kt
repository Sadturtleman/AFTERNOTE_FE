package com.kuit.afternote.feature.user.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.feature.user.domain.usecase.GetReceiverDailyQuestionsUseCase
import com.kuit.afternote.feature.user.presentation.uimodel.DailyQuestionAnswerItemUi
import com.kuit.afternote.feature.user.presentation.uimodel.ReceiverDailyQuestionsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 수신인별 데일리 질문 답변 목록 화면 ViewModel.
 * GET /users/receivers/{receiverId}/daily-questions
 */
@HiltViewModel
class ReceiverDailyQuestionsViewModel
    @Inject
    constructor(
        private val getReceiverDailyQuestionsUseCase: GetReceiverDailyQuestionsUseCase
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(ReceiverDailyQuestionsUiState())
        val uiState: StateFlow<ReceiverDailyQuestionsUiState> = _uiState.asStateFlow()

        fun loadDailyQuestions(receiverId: Long) {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                getReceiverDailyQuestionsUseCase(receiverId = receiverId)
                    .onSuccess { list ->
                        _uiState.update {
                            it.copy(
                                items = list.map { item ->
                                    DailyQuestionAnswerItemUi(
                                        dailyQuestionAnswerId = item.dailyQuestionAnswerId,
                                        question = item.question,
                                        answer = item.answer,
                                        createdAt = item.createdAt
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
                                errorMessage = e.message ?: "데일리 질문 답변 목록 조회에 실패했습니다."
                            )
                        }
                    }
            }
        }

        fun clearError() {
            _uiState.update { it.copy(errorMessage = null) }
        }
    }
