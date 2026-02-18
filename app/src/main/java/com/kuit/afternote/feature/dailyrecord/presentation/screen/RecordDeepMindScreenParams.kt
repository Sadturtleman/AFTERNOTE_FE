package com.kuit.afternote.feature.dailyrecord.presentation.screen

import androidx.compose.runtime.Immutable

/**
 * 파라미터 그룹: 깊은 생각 화면 카테고리(기록 주제) 상태 및 콜백
 */
@Immutable
data class RecordCategoryState(
    val selectedCategory: String,
    val onCategoryChange: (String) -> Unit,
    val showCategoryDropdown: Boolean,
    val onCategoryClick: () -> Unit,
    val onCategoryDropdownDismiss: () -> Unit
)

/**
 * RecordDeepMindScreen 단일 파라미터 (S107 준수)
 */
@Immutable
data class RecordDeepMindScreenParams(
    val contentState: RecordDiaryContentState,
    val dateState: RecordDiaryDateState,
    val categoryState: RecordCategoryState,
    val onLeftClick: () -> Unit,
    val onRegisterClick: () -> Unit
)
