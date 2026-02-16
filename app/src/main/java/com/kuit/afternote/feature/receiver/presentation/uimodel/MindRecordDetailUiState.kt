package com.kuit.afternote.feature.receiver.presentation.uimodel

import java.time.LocalDate

/**
 * 마음의 기록 상세(날짜별) 화면 한 건 UI 모델.
 * ExpandableRecordItem에 바인딩합니다.
 */
data class MindRecordItemUiModel(
    val mindRecordId: Long,
    val date: String,
    val tags: String,
    val question: String,
    val content: String,
    val hasImage: Boolean
)

/**
 * 마음의 기록 상세 화면 UiState.
 */
data class MindRecordDetailUiState(
    val mindRecordItems: List<MindRecordItemUiModel> = emptyList(),
    val selectedDate: LocalDate = LocalDate.now(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
