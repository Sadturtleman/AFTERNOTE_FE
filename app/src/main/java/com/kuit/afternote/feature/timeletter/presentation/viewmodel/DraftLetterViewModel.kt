package com.kuit.afternote.feature.timeletter.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.feature.timeletter.domain.model.TimeLetter
import com.kuit.afternote.feature.timeletter.domain.usecase.DeleteAllTemporaryUseCase
import com.kuit.afternote.feature.timeletter.domain.usecase.DeleteTimeLettersUseCase
import com.kuit.afternote.feature.timeletter.domain.usecase.GetTemporaryTimeLettersUseCase
import com.kuit.afternote.feature.timeletter.presentation.screen.DraftLetterItem
import com.kuit.afternote.feature.user.domain.model.ReceiverListItem
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
 * 임시저장 편지 목록 화면 ViewModel
 *
 * GetTemporaryTimeLettersUseCase, DeleteTimeLettersUseCase, DeleteAllTemporaryUseCase를 통해
 * 임시저장 목록 조회/삭제를 처리합니다.
 */
@HiltViewModel
class DraftLetterViewModel
    @Inject
    constructor(
        private val getTemporaryTimeLettersUseCase: GetTemporaryTimeLettersUseCase,
        private val deleteTimeLettersUseCase: DeleteTimeLettersUseCase,
        private val deleteAllTemporaryUseCase: DeleteAllTemporaryUseCase,
        private val getUserIdUseCase: GetUserIdUseCase,
        private val getReceiversUseCase: GetReceiversUseCase
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(DraftLetterUiState())
        val uiState: StateFlow<DraftLetterUiState> = _uiState.asStateFlow()

        /**
         * 임시저장 목록 로드 (GET /time-letters/temporary)
         * 수신자 목록(GET /users/receivers)으로 receiverIds → 이름 매핑 후 표시
         */
        fun loadTemporaryLetters() {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true) }
                val receivers = loadReceiversOrEmpty()
                getTemporaryTimeLettersUseCase()
                    .onSuccess { list ->
                        val items = list.timeLetters.map { toDraftLetterItem(it, receivers) }
                        _uiState.update {
                            it.copy(
                                draftLetters = items,
                                isLoading = false
                            )
                        }
                    }.onFailure {
                        _uiState.update {
                            it.copy(
                                draftLetters = emptyList(),
                                isLoading = false
                            )
                        }
                    }
            }
        }

        private suspend fun loadReceiversOrEmpty(): List<ReceiverListItem> {
            val userId = getUserIdUseCase() ?: return emptyList()
            return getReceiversUseCase(userId).getOrNull() ?: emptyList()
        }

        private fun resolveReceiverDisplayText(
            receiverIds: List<Long>,
            receivers: List<ReceiverListItem>
        ): String =
            when {
                receiverIds.isEmpty() -> "-"
                receiverIds.size == 1 -> {
                    receivers.find { it.receiverId == receiverIds[0] }?.name?.let { name -> "${name}님께" } ?: "-"
                }
                else -> "${receiverIds.size}명에게"
            }

        /**
         * 편집 모드 진입
         */
        fun enterEditMode() {
            _uiState.update {
                it.copy(isEditMode = true, selectedIds = emptySet())
            }
        }

        /**
         * 편집 모드 완료
         */
        fun exitEditMode() {
            _uiState.update {
                it.copy(isEditMode = false, selectedIds = emptySet())
            }
        }

        /**
         * 아이템 선택/해제 토글
         */
        fun toggleSelection(id: String) {
            _uiState.update { state ->
                val newSet = if (state.selectedIds.contains(id)) {
                    state.selectedIds - id
                } else {
                    state.selectedIds + id
                }
                state.copy(selectedIds = newSet)
            }
        }

        /**
         * 선택된 항목 삭제 (POST /time-letters/delete)
         */
        fun deleteSelected(onSuccess: () -> Unit) {
            viewModelScope.launch {
                val ids = _uiState.value.selectedIds.mapNotNull { it.toLongOrNull() }
                if (ids.isEmpty()) return@launch
                deleteTimeLettersUseCase(ids)
                    .onSuccess {
                        _uiState.update {
                            it.copy(
                                isEditMode = false,
                                selectedIds = emptySet()
                            )
                        }
                        loadTemporaryLetters()
                        onSuccess()
                    }
            }
        }

        /**
         * 전체 임시저장 삭제 (DELETE /time-letters/temporary)
         */
        fun deleteAll(onSuccess: () -> Unit) {
            viewModelScope.launch {
                deleteAllTemporaryUseCase()
                    .onSuccess {
                        _uiState.update {
                            it.copy(
                                isEditMode = false,
                                selectedIds = emptySet(),
                                draftLetters = emptyList()
                            )
                        }
                        onSuccess()
                    }
            }
        }

        private fun toDraftLetterItem(
            t: TimeLetter,
            receivers: List<ReceiverListItem>
        ): DraftLetterItem =
            DraftLetterItem(
                id = t.id.toString(),
                receiverName = resolveReceiverDisplayText(t.receiverIds, receivers),
                sendDate = formatSendAtForDisplay(t.sendAt),
                title = t.title ?: ""
            )

        /**
         * sendAt (yyyy-MM-ddTHH:mm:ss 등) -> "yyyy. MM. dd" 표시
         */
        private fun formatSendAtForDisplay(sendAt: String?): String {
            if (sendAt.isNullOrBlank()) return ""
            val datePart = sendAt.take(10) // yyyy-MM-dd
            return datePart.replace("-", ". ")
        }
    }

/**
 * 임시저장 목록 화면 UI 상태
 */
data class DraftLetterUiState(
    val draftLetters: List<DraftLetterItem> = emptyList(),
    val isLoading: Boolean = false,
    val isEditMode: Boolean = false,
    val selectedIds: Set<String> = emptySet()
)
