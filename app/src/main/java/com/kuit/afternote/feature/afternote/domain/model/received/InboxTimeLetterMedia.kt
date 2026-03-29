package com.kuit.afternote.feature.afternote.domain.model.received

/** 타임레터 첨부 미디어 (수신자 인증번호 플로우 / 전체 다운로드 묶음용). */
data class InboxTimeLetterMedia(
    val id: Long,
    val mediaType: String?,
    val mediaUrl: String,
)
