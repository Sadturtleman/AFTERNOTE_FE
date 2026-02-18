package com.kuit.afternote.feature.timeletter.domain.model

/**
 * TimeLetter 도메인 모델. (스웨거 기준)
 */

enum class TimeLetterStatus {
    DRAFT,
    SCHEDULED,
    SENT
}

enum class TimeLetterMediaType {
    IMAGE,
    VIDEO,
    AUDIO,
    DOCUMENT
}

data class TimeLetterMedia(
    val id: Long,
    val mediaType: TimeLetterMediaType,
    val mediaUrl: String
)

data class TimeLetter(
    val id: Long,
    val title: String?,
    val content: String?,
    val sendAt: String?,
    val status: TimeLetterStatus,
    val mediaList: List<TimeLetterMedia>,
    val createdAt: String?,
    val updatedAt: String?,
    val receiverIds: List<Long> = emptyList()
)

data class TimeLetterList(
    val timeLetters: List<TimeLetter>,
    val totalCount: Int
)
