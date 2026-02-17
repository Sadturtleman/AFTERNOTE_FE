package com.kuit.afternote.feature.dailyrecord.presentation.uimodel

data class MindRecordUiState(
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
    /** Error message when save fails (e.g. network). Cleared on next save attempt or success. */
    val saveErrorMessage: String? = null
)
