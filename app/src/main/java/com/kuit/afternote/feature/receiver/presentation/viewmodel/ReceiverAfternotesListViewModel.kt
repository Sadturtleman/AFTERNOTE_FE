package com.kuit.afternote.feature.receiver.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.feature.receiver.domain.usecase.GetAfterNotesByAuthCodeUseCase
import com.kuit.afternote.feature.receiver.presentation.uimodel.ReceivedAfternoteListItemUi
import com.kuit.afternote.feature.receiver.presentation.uimodel.ReceiverAfternotesListUiState
import com.kuit.afternote.feature.receiverauth.session.ReceiverAuthSessionHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 수신인별 애프터노트 목록 화면 ViewModel.
 *
 * GET /api/receiver-auth/after-notes (X-Auth-Code) API로 전달된 애프터노트 목록을 조회합니다.
 */
@HiltViewModel
class ReceiverAfternotesListViewModel
    @Inject
    constructor(
        private val receiverAuthSessionHolder: ReceiverAuthSessionHolder,
        private val getAfterNotesByAuthCodeUseCase: GetAfterNotesByAuthCodeUseCase
    ) : ViewModel() {

        private val _uiState = MutableStateFlow(ReceiverAfternotesListUiState())
        val uiState: StateFlow<ReceiverAfternotesListUiState> = _uiState.asStateFlow()

        init {
            receiverAuthSessionHolder.getAuthCode()?.let { authCode -> loadAfterNotes(authCode) }
        }

        fun loadAfterNotes(authCode: String) {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                getAfterNotesByAuthCodeUseCase(authCode)
                    .onSuccess { data ->
                        _uiState.update {
                            it.copy(
                                items = data.items.map { item ->
                                    ReceivedAfternoteListItemUi(
                                        id = item.id,
                                        title = item.title.orEmpty(),
                                        sourceType = item.sourceType.orEmpty(),
                                        lastUpdatedAt = item.lastUpdatedAt.orEmpty()
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
                                errorMessage = e.message ?: "애프터노트 목록 조회에 실패했습니다."
                            )
                        }
                    }
            }
        }

        fun clearError() {
            _uiState.update { it.copy(errorMessage = null) }
        }

        /**
         * 에러 상태에서 재시도. 세션의 authCode로 목록을 다시 로드합니다.
         */
        fun retry() {
            clearError()
            receiverAuthSessionHolder.getAuthCode()?.let { loadAfterNotes(it) }
        }
    }
