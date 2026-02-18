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
    val mindRecordDeliveryEnabled: Boolean = true,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class EditReceiverUiState(
    val receiverId: Long = 0L,
    val name: String = "",
    val relation: String = "",
    val phone: String = "",
    val email: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val updateSuccess: Boolean = false
)

data class DailyQuestionAnswerItemUi(
    val dailyQuestionAnswerId: Long,
    val question: String,
    val answer: String,
    val recordDate: String
)

data class ReceiverDailyQuestionsUiState(
    val items: List<DailyQuestionAnswerItemUi> = emptyList(),
    val hasNext: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
