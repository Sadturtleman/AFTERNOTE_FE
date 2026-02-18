package com.kuit.afternote.feature.timeletter.presentation.uimodel

import com.kuit.afternote.feature.timeletter.presentation.component.LetterTheme

sealed class TimeLetterUiState {
    object Loading : TimeLetterUiState()

    object Empty : TimeLetterUiState()

    data class Success(
        val letters: List<TimeLetterItem>,
        /** 수신자 필터 적용 시 해당 수신자 이름. null이면 "전체보기" 표시 */
        val selectedReceiverName: String? = null
    ) : TimeLetterUiState()
}

data class TimeLetterItem(
    val id: String,
    val receivername: String,
    val sendDate: String,
    val title: String,
    val content: String,
    val imageResId: Int? = null,
    val createDate: String,
    val theme: LetterTheme = LetterTheme.BLUE,
    val mediaUrls: List<String> = emptyList(),
    val audioUrls: List<String> = emptyList()
)
