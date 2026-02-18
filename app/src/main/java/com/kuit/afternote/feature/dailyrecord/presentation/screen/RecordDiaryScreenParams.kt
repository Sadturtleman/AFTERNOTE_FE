package com.kuit.afternote.feature.dailyrecord.presentation.screen

import androidx.compose.runtime.Immutable

/**
 * 파라미터 그룹: 일기 화면 제목/내용 및 변경 콜백
 */
@Immutable
data class RecordDiaryContentState(
    val title: String,
    val content: String,
    val onTitleChange: (String) -> Unit,
    val onContentChange: (String) -> Unit
)

/**
 * 파라미터 그룹: 일기 화면 날짜 선택 상태 및 콜백
 */
@Immutable
data class RecordDiaryDateState(
    val sendDate: String,
    val showDatePicker: Boolean,
    val onDateClick: () -> Unit,
    val onDatePickerDismiss: () -> Unit,
    val onDateSelected: (Int, Int, Int) -> Unit
)

/**
 * RecordDiaryScreen 단일 파라미터 (S107 준수)
 */
@Immutable
data class RecordDiaryScreenParams(
    val contentState: RecordDiaryContentState,
    val dateState: RecordDiaryDateState,
    val onLeftClick: () -> Unit,
    val onRegisterClick: () -> Unit
)
