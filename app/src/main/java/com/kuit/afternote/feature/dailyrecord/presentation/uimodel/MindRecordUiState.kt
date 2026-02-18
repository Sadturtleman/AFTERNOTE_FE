package com.kuit.afternote.feature.dailyrecord.presentation.uimodel

data class MindRecordUiState(
    /** 오늘의 데일리 질문 텍스트 (GET /daily-question content). null이면 로딩 중 또는 실패. */
    val dailyQuestionText: String? = null,
    /** 기록 생성 실패 시 에러 메시지. null이면 에러 없음. */
    val createErrorMessage: String? = null,
    val title: String = "",
    val content: String = "",
    val sendDate: String = "",
    val isLoading: Boolean = false,
    val isSaveEnabled: Boolean = false,
    val showDatePicker: Boolean = false,
    val showTimePicker: Boolean = false,
    val draftCount: Int = 0,
    val showWritingPlusMenu: Boolean = false,
    val showRecipientDropdown: Boolean = false,
    val showRegisteredPopUp: Boolean = false,
    val showDraftSavePopUp: Boolean = false,
    val showWaitingAgainPopUp: Boolean = false,
    val draftId: Long? = null,
    /** 깊은 생각 카테고리 (나의 가치관, 오늘 떠올린 생각, 인생을 되돌아 보며) */
    val selectedCategory: String = "나의 가치관",
    val showCategoryDropdown: Boolean = false
)
