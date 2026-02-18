package com.kuit.afternote.feature.receiver.presentation.uimodel

import java.time.LocalDate

/**
 * 마음의 기록 캘린더(목록) 화면 UiState.
 */
data class MindRecordListUiState(
    val selectedDate: LocalDate = LocalDate.now(),
    val daysWithRecords: Set<Int> = emptySet(),
    val selectedDateRecords: List<MindRecordItemUiModel> = emptyList(),
    val todayRecord: MindRecordItemUiModel? = null,
    /** 오늘의 기록 카드용 대표 이미지 URL. 상세 API imageList 첫 번째 항목. */
    val todayRecordImageUrl: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val showDatePicker: Boolean = false
)
