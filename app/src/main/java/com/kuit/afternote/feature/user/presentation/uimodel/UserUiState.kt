package com.kuit.afternote.feature.user.presentation.uimodel

/**
 * User UI 상태 모델.
 */

data class ProfileUiState(
    val name: String = "",
    val email: String = "",
    val phone: String? = null,
    val savedProfileImageUrl: String? = null,
    val pickedProfileImageUri: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val updateSuccess: Boolean = false
)

data class PushSettingsUiState(
    val timeLetter: Boolean = false,
    val mindRecord: Boolean = false,
    val afterNote: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val updateSuccess: Boolean = false
)

// --- Receivers ---

data class ReceiverListItemUi(
    val receiverId: Long,
    val name: String,
    val relation: String
)

data class ReceiverListUiState(
    val receivers: List<ReceiverListItemUi> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class RegisterReceiverUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val registeredReceiverId: Long? = null
)

data class ReceiverDetailUiState(
    val receiverId: Long = 0L,
    val name: String = "",
    val relation: String = "",
    val phone: String? = null,
    val email: String? = null,
    val dailyQuestionCount: Int = 0,
    val timeLetterCount: Int = 0,
    val afterNoteCount: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class DailyQuestionAnswerItemUi(
    val dailyQuestionAnswerId: Long,
    val question: String,
    val answer: String,
    val createdAt: String
)

data class ReceiverDailyQuestionsUiState(
    val items: List<DailyQuestionAnswerItemUi> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class ReceiverTimeLetterItemUi(
    val timeLetterId: Long,
    val receiverName: String,
    val sendAt: String,
    val title: String,
    val content: String
)

data class ReceiverTimeLettersUiState(
    val items: List<ReceiverTimeLetterItemUi> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class ReceiverAfterNoteSourceItemUi(
    val sourceType: String,
    val lastUpdatedAt: String
)

data class ReceiverAfterNotesUiState(
    val items: List<ReceiverAfterNoteSourceItemUi> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
