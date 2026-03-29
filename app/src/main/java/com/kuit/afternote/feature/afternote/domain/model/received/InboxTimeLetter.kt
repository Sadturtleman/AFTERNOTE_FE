package com.kuit.afternote.feature.afternote.domain.model.received

/** 수신자 인증번호로 조회한 타임레터 한 건 (export·다운로드 묶음용). */
data class InboxTimeLetter(
    val timeLetterId: Long,
    val timeLetterReceiverId: Long,
    val title: String?,
    val content: String?,
    val sendAt: String?,
    val status: String?,
    val senderName: String?,
    val deliveredAt: String?,
    val createdAt: String?,
    val mediaList: List<InboxTimeLetterMedia> = emptyList(),
    val isRead: Boolean = false,
)
