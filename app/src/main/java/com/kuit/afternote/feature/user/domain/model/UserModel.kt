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
    val relation: String,
    val mindRecordDeliveryEnabled: Boolean = true
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

/** 수신인별 마음의 기록 항목 (일기, 깊은 생각, 데일리 질문 답변). */
data class ReceiverMindRecordItem(
    val recordId: Long,
    val type: String,
    val titleOrQuestion: String,
    val contentOrAnswer: String,
    val recordDate: String
)

/** Paginated result for GET /users/receivers/{receiverId}/mind-records. */
data class ReceiverMindRecordsResult(
    val items: List<ReceiverMindRecordItem>,
    val hasNext: Boolean
)

// --- Delivery condition (GET/PATCH /users/delivery-condition) ---

/** 전달 조건 타입 - 콘텐츠가 수신자에게 전달되는 조건. */
enum class DeliveryConditionType {
    NONE,
    DEATH_CERTIFICATE,
    INACTIVITY,
    SPECIFIC_DATE
}

/** 전달 조건 설정 (조회/수정 응답). */
data class DeliveryCondition(
    val conditionType: DeliveryConditionType,
    val inactivityPeriodDays: Int?,
    val specificDate: String?,
    val leaveMessage: String? = null,
    val conditionFulfilled: Boolean,
    val conditionMet: Boolean
)
