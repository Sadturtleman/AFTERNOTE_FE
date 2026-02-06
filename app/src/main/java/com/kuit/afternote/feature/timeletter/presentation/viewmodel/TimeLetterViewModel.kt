package com.kuit.afternote.feature.timeletter.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.feature.timeletter.domain.model.TimeLetter
import com.kuit.afternote.feature.timeletter.domain.usecase.DeleteTimeLettersUseCase
import com.kuit.afternote.feature.timeletter.domain.usecase.GetTimeLettersUseCase
import com.kuit.afternote.feature.timeletter.presentation.component.LetterTheme
import com.kuit.afternote.feature.timeletter.presentation.uimodel.TimeLetterItem
import com.kuit.afternote.feature.timeletter.presentation.uimodel.TimeLetterUiState
import com.kuit.afternote.feature.timeletter.presentation.uimodel.ViewMode
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
        private val deleteTimeLettersUseCase: DeleteTimeLettersUseCase
    ) : ViewModel() {
        private val _viewMode = MutableStateFlow(ViewMode.LIST)
        val viewMode: StateFlow<ViewMode> = _viewMode.asStateFlow()

        private val _uiState = MutableStateFlow<TimeLetterUiState>(TimeLetterUiState.Loading)
        val uiState: StateFlow<TimeLetterUiState> = _uiState.asStateFlow()

        init {
            loadLetters()
        }

        /**
         * 뷰 모드 변경 (리스트 / 블록)
         */
        fun updateViewMode(mode: ViewMode) {
            _viewMode.value = mode
        }

        /**
         * 타임레터 목록 로드 (GET /time-letters)
         */
        private fun loadLetters() {
            viewModelScope.launch {
                _uiState.value = TimeLetterUiState.Loading
                getTimeLettersUseCase()
                    .onSuccess { list ->
                        val items = list.timeLetters.mapIndexed { index, timeLetter ->
                            toTimeLetterItem(timeLetter, index)
                        }
                        _uiState.value = if (items.isEmpty()) {
                            TimeLetterUiState.Empty
                        } else {
                            TimeLetterUiState.Success(items)
                        }
                    }.onFailure {
                        _uiState.value = TimeLetterUiState.Empty
                    }
            }
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
         */
        private fun toTimeLetterItem(
            t: TimeLetter,
            index: Int
        ): TimeLetterItem {
            val themes = listOf(LetterTheme.PEACH, LetterTheme.BLUE, LetterTheme.YELLOW)
            return TimeLetterItem(
                id = t.id.toString(),
                receivername = "", // API에 수신자 필드 없음
                sendDate = formatSendAtForDisplay(t.sendAt),
                title = t.title ?: "",
                content = t.content ?: "",
                imageResId = null,
                theme = themes[index % themes.size]
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
