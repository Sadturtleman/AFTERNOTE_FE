package com.kuit.afternote.feature.user.domain.model

/**
 * User 도메인 모델. (스웨거 기준)
 */

data class UserProfile(
    val name: String,
    val email: String,
    val phone: String?,
    val profileImageUrl: String?
)

data class PushSettings(
    val timeLetter: Boolean,
    val mindRecord: Boolean,
    val afterNote: Boolean
)

// --- Receivers ---

data class ReceiverListItem(
    val receiverId: Long,
    val name: String,
    val relation: String
)

data class ReceiverDetail(
    val receiverId: Long,
    val name: String,
    val relation: String,
    val phone: String?,
    val email: String?,
    val dailyQuestionCount: Int,
    val timeLetterCount: Int,
    val afterNoteCount: Int
)

data class DailyQuestionAnswerItem(
    val dailyQuestionAnswerId: Long,
    val question: String,
    val answer: String,
    val recordDate: String
)

/** Paginated result for GET /users/receivers/{receiverId}/daily-questions. */
data class ReceiverDailyQuestionsResult(
    val items: List<DailyQuestionAnswerItem>,
    val hasNext: Boolean
)

data class ReceiverTimeLetterItem(
    val timeLetterId: Long,
    val receiverName: String,
    val sendAt: String,
    val title: String,
    val content: String
)

data class ReceiverAfterNoteSourceItem(
    val sourceType: String,
    val lastUpdatedAt: String
)
