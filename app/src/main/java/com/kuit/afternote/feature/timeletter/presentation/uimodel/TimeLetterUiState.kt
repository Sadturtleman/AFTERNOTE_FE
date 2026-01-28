package com.kuit.afternote.feature.timeletter.presentation.uimodel

import com.kuit.afternote.feature.timeletter.presentation.component.LetterTheme

sealed class TimeLetterUiState {
    object Loading : TimeLetterUiState()

    object Empty : TimeLetterUiState()

    data class Success(
        val letters: List<TimeLetterItem>
    ) : TimeLetterUiState()
}

data class TimeLetterItem(
    val id: String,
    val receivername: String,
    val sendDate: String,
    val title: String,
    val content: String,
    val imageResId: Int? = null,
    val theme: LetterTheme = LetterTheme.BLUE
)
