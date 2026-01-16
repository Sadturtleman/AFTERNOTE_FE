package com.kuit.afternote.feature.timeletter.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.feature.timeletter.presentation.uimodel.TimeLetterItem
import com.kuit.afternote.feature.timeletter.presentation.uimodel.TimeLetterUiState
import com.kuit.afternote.feature.timeletter.presentation.uimodel.ViewMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 타임레터 메인 화면의 ViewModel
 *
 * 타임레터 목록을 관리하고 뷰 모드(리스트/블록)를 제어합니다.
 */
class TimeLetterViewModel : ViewModel() {

    private val _viewMode = MutableStateFlow(ViewMode.LIST)
    val viewMode: StateFlow<ViewMode> = _viewMode.asStateFlow()

    private val _uiState = MutableStateFlow<TimeLetterUiState>(TimeLetterUiState.Loading)
    val uiState: StateFlow<TimeLetterUiState> = _uiState.asStateFlow()

    init {
        loadLetters()
    }

    fun updateViewMode(mode: ViewMode) {
        _viewMode.value = mode
    }

    private fun loadLetters() {
        viewModelScope.launch {
            // TODO: 실제 데이터 로드 로직 (Repository 호출)
            // 예시: val letters = repository.getTimeLetters()

            // 테스트용 목업 데이터
            val letters = listOf(
                TimeLetterItem(
                    id = "1",
                    receivername = "박채연",
                    sendDate = "2027. 11. 24",
                    title = "채연아 20번째 생일을 축하해",
                    content = "너가 태어난 게 엊그제같은데 벌써 스무살이라니..엄마가 없어도 씩씩하게 컸을 채연이를 상상하면 너무 기특해서 안아주고 싶...",
                    imageResId = null,
                    theme = com.kuit.afternote.feature.timeletter.presentation.component.LetterTheme.PEACH
                ),
                TimeLetterItem(
                    id = "2",
                    receivername = "김민수",
                    sendDate = "2026. 05. 10",
                    title = "졸업 축하해 친구야",
                    content = "드디어 졸업이구나! 우리가 함께한 시간들이 정말 소중했어. 앞으로도 좋은 일만 가득하길...",
                    imageResId = null,
                    theme = com.kuit.afternote.feature.timeletter.presentation.component.LetterTheme.BLUE
                ),
                TimeLetterItem(
                    id = "3",
                    receivername = "이지은",
                    sendDate = "2028. 01. 01",
                    title = "새해 복 많이 받아",
                    content = "새해가 밝았어! 올해도 건강하고 행복하게 보내길 바라. 사랑해!",
                    imageResId = null,
                    theme = com.kuit.afternote.feature.timeletter.presentation.component.LetterTheme.YELLOW
                )
            )

            _uiState.value = if (letters.isEmpty()) {
                TimeLetterUiState.Empty
            } else {
                TimeLetterUiState.Success(letters)
            }
        }
    }

    // 데이터 새로고침용
    fun refreshLetters() {
        _uiState.value = TimeLetterUiState.Loading
        loadLetters()
    }
}
