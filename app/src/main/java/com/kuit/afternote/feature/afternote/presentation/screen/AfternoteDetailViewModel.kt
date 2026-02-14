package com.kuit.afternote.feature.afternote.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.feature.afternote.domain.usecase.DeleteAfternoteUseCase
import com.kuit.afternote.feature.afternote.domain.usecase.GetAfternoteDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 애프터노트 상세 화면 ViewModel.
 *
 * - 상세 조회: GET /api/afternotes/{id}
 * - 삭제: DELETE /api/afternotes/{id}
 */
@HiltViewModel
class AfternoteDetailViewModel
    @Inject
    constructor(
        private val getDetailUseCase: GetAfternoteDetailUseCase,
        private val deleteUseCase: DeleteAfternoteUseCase
    ) : ViewModel() {

        private val _uiState = MutableStateFlow(AfternoteDetailUiState())
        val uiState: StateFlow<AfternoteDetailUiState> = _uiState.asStateFlow()

        fun loadDetail(afternoteId: Long) {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, error = null) }
                getDetailUseCase(afternoteId = afternoteId)
                    .onSuccess { detail ->
                        _uiState.update {
                            it.copy(isLoading = false, detail = detail, error = null)
                        }
                    }
                    .onFailure { e ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = e.message ?: "상세 정보를 불러오지 못했습니다."
                            )
                        }
                    }
            }
        }

        fun deleteAfternote(afternoteId: Long) {
            viewModelScope.launch {
                _uiState.update { it.copy(isDeleting = true, deleteError = null) }
                deleteUseCase(afternoteId = afternoteId)
                    .onSuccess {
                        _uiState.update {
                            it.copy(isDeleting = false, deleteSuccess = true)
                        }
                    }
                    .onFailure { e ->
                        _uiState.update {
                            it.copy(
                                isDeleting = false,
                                deleteError = e.message ?: "삭제에 실패했습니다."
                            )
                        }
                    }
            }
        }

        fun clearDeleteResult() {
            _uiState.update { it.copy(deleteSuccess = false, deleteError = null) }
        }
    }
