package com.kuit.afternote.feature.timeletter.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.feature.timeletter.presentation.navgraph.SELECTED_RECEIVER_ID_KEY
import com.kuit.afternote.feature.timeletter.domain.model.TimeLetter
import com.kuit.afternote.feature.timeletter.domain.model.TimeLetterMediaType
import com.kuit.afternote.feature.timeletter.domain.usecase.DeleteTimeLettersUseCase
import com.kuit.afternote.feature.timeletter.domain.usecase.GetTimeLettersUseCase
import com.kuit.afternote.feature.timeletter.presentation.component.LetterTheme
import com.kuit.afternote.feature.timeletter.presentation.uimodel.TimeLetterItem
import com.kuit.afternote.feature.timeletter.presentation.uimodel.TimeLetterUiState
import com.kuit.afternote.feature.timeletter.presentation.uimodel.ViewMode
import com.kuit.afternote.feature.user.domain.model.ReceiverListItem
import com.kuit.afternote.feature.user.domain.usecase.GetReceiversUseCase
import com.kuit.afternote.feature.user.domain.usecase.GetUserIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 타임레터 메인 화면의 ViewModel
 *
 * 타임레터 목록을 관리하고 뷰 모드(리스트/블록)를 제어합니다.
 * GetTimeLettersUseCase를 통해 데이터를 조회합니다.
 */
@HiltViewModel
class TimeLetterViewModel
    @Inject
    constructor(
        private val getTimeLettersUseCase: GetTimeLettersUseCase,
        private val deleteTimeLettersUseCase: DeleteTimeLettersUseCase,
        private val getUserIdUseCase: GetUserIdUseCase,
        private val getReceiversUseCase: GetReceiversUseCase,
        private val savedStateHandle: SavedStateHandle
    ) : ViewModel() {
        private val _viewMode = MutableStateFlow(ViewMode.LIST)
        val viewMode: StateFlow<ViewMode> = _viewMode.asStateFlow()

        private val _uiState = MutableStateFlow<TimeLetterUiState>(TimeLetterUiState.Loading)
        val uiState: StateFlow<TimeLetterUiState> = _uiState.asStateFlow()

        /**
         * 뷰 모드 변경 (리스트 / 블록)
         */
        fun updateViewMode(mode: ViewMode) {
            _viewMode.value = mode
        }

        /**
         * 타임레터 목록 로드 (GET /time-letters)
         * 수신자 목록(GET /users/receivers)으로 receiverIds → 이름 매핑 후 표시.
         * SavedStateHandle에 선택 수신자 ID가 있으면 해당 수신자로 필터링하고 헤더에 이름을 노출한다.
         */
        private fun loadLetters() {
            viewModelScope.launch {
                _uiState.value = TimeLetterUiState.Loading
                val receivers = loadReceiversOrEmpty()
                val selectedReceiverId = savedStateHandle[SELECTED_RECEIVER_ID_KEY] as? Long
                getTimeLettersUseCase()
                    .onSuccess { list ->
                        val filteredLetters =
                            if (selectedReceiverId != null) {
                                list.timeLetters.filter { it.receiverIds.contains(selectedReceiverId) }
                            } else {
                                list.timeLetters
                            }
                        val items = filteredLetters.mapIndexed { index, timeLetter ->
                            toTimeLetterItem(timeLetter, index, receivers)
                        }
                        val selectedReceiverName =
                            selectedReceiverId?.let { id ->
                                receivers.find { it.receiverId == id }?.name
                            }
                        _uiState.value = if (items.isEmpty()) {
                            TimeLetterUiState.Empty
                        } else {
                            TimeLetterUiState.Success(
                                letters = items,
                                selectedReceiverName = selectedReceiverName
                            )
                        }
                    }.onFailure {
                        _uiState.value = TimeLetterUiState.Empty
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
         * 데이터 새로고침
         */
        fun refreshLetters() {
            loadLetters()
        }

        /**
         * 타임레터 삭제 (deleteTimeLetters API 호출)
         *
         * @param id 삭제할 타임레터 ID
         */
        fun deleteTimeLetter(id: String) {
            val longId = id.toLongOrNull() ?: return
            viewModelScope.launch {
                deleteTimeLettersUseCase(listOf(longId))
                    .onSuccess { refreshLetters() }
            }
        }

        /**
         * Domain TimeLetter → UI TimeLetterItem 변환
         * receivername은 receiverIds + 수신자 목록으로 해석
         */
        private fun toTimeLetterItem(
            t: TimeLetter,
            index: Int,
            receivers: List<ReceiverListItem>
        ): TimeLetterItem {
            val themes = listOf(LetterTheme.PEACH, LetterTheme.BLUE, LetterTheme.YELLOW)
            val imageUrls = t.mediaList
                .filter { it.mediaType == TimeLetterMediaType.IMAGE }
                .map { it.mediaUrl }
            val audioUrls = t.mediaList
                .filter { it.mediaType == TimeLetterMediaType.AUDIO }
                .map { it.mediaUrl }
            val linkUrls = t.mediaList
                .filter { it.mediaType == TimeLetterMediaType.DOCUMENT }
                .map { it.mediaUrl }
            return TimeLetterItem(
                id = t.id.toString(),
                receivername = resolveReceiverDisplayText(t.receiverIds, receivers),
                sendDate = formatSendAtForDisplay(t.sendAt),
                title = t.title ?: "",
                content = t.content ?: "",
                imageResId = null,
                theme = themes[index % themes.size],
                createDate = formatSendAtForDisplay(t.createdAt),
                mediaUrls = imageUrls,
                audioUrls = audioUrls,
                linkUrls = linkUrls
            )
        }

        /**
         * sendAt (yyyy-MM-ddTHH:mm:ss 등) -> "yyyy. MM. dd" 표시
         */
        private fun formatSendAtForDisplay(sendAt: String?): String {
            if (sendAt.isNullOrBlank()) return ""
            val datePart = sendAt.take(10) // yyyy-MM-dd
            return datePart.replace("-", ". ")
        }
    }
