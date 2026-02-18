package com.kuit.afternote.feature.dailyrecord.presentation.component

import androidx.compose.runtime.Immutable

/**
 * 파라미터 그룹: RecordDiaryContentItem 날짜 섹션
 */
@Immutable
data class RecordDiaryDateSectionParams(
    val onDateClick: () -> Unit,
    val sendDate: String,
    val showDatePicker: Boolean = false,
    val onDatePickerDismiss: () -> Unit
)

/**
 * 파라미터 그룹: RecordDiaryContentItem 카테고리(기록 주제) 섹션
 */
@Immutable
data class RecordDiaryCategorySectionParams(
    val selectedCategory: String = "나의 가치관",
    val onCategoryChange: (String) -> Unit = {},
    val showCategoryDropdown: Boolean = false,
    val onCategoryClick: () -> Unit = {},
    val onCategoryDropdownDismiss: () -> Unit = {}
)

/**
 * RecordDiaryContentItem 단일 파라미터 (S107 준수)
 */
@Immutable
data class RecordDiaryContentItemParams(
    val standard: String,
    val onDateSelected: (Int, Int, Int) -> Unit,
    val title: String,
    val onTitleChange: (String) -> Unit,
    val content: String,
    val onContentChange: (String) -> Unit,
    val dateSection: RecordDiaryDateSectionParams,
    val categorySection: RecordDiaryCategorySectionParams = RecordDiaryCategorySectionParams()
)
