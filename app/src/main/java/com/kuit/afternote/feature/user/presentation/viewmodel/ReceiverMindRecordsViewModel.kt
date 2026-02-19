package com.kuit.afternote.feature.user.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kuit.afternote.feature.setting.presentation.navgraph.SettingRoute
import com.kuit.afternote.feature.user.domain.usecase.GetReceiverMindRecordsUseCase
import com.kuit.afternote.feature.user.presentation.uimodel.ReceiverMindRecordItemUi
import com.kuit.afternote.feature.user.presentation.uimodel.ReceiverMindRecordsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 수신인별 마음의 기록 화면 ViewModel (일기, 깊은 생각, 데일리 질문 답변).
 * GET /users/receivers/{receiverId}/mind-records
 */
@HiltViewModel
class ReceiverMindRecordsViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val getReceiverMindRecordsUseCase: GetReceiverMindRecordsUseCase
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(ReceiverMindRecordsUiState())
        val uiState: StateFlow<ReceiverMindRecordsUiState> = _uiState.asStateFlow()

        init {
            val receiverId = savedStateHandle.toRoute<SettingRoute.DailyAnswerRoute>()
                .receiverId.toLongOrNull()
            if (receiverId != null) loadMindRecords(receiverId)
        }

        fun loadMindRecords(receiverId: Long, page: Int = 0, size: Int = 20) {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                getReceiverMindRecordsUseCase(
                    receiverId = receiverId,
                    page = page,
                    size = size
                )
                    .onSuccess { result ->
                        _uiState.update {
                            it.copy(
                                items = result.items.map { item ->
                                    ReceiverMindRecordItemUi(
                                        recordId = item.recordId,
                                        type = item.type,
                                        titleOrQuestion = item.titleOrQuestion,
                                        contentOrAnswer = item.contentOrAnswer,
                                        recordDate = item.recordDate
                                    )
                                },
                                hasNext = result.hasNext,
                                isLoading = false,
                                errorMessage = null
                            )
                        }
                    }
                    .onFailure { e ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = e.message ?: "마음의 기록 목록 조회에 실패했습니다."
                            )
                        }
                    }
            }
        }

        fun clearError() {
            _uiState.update { it.copy(errorMessage = null) }
        }
    }
