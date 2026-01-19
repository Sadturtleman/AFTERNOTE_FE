package com.kuit.afternote.feature.timeletter.domain.entity

/**
 * 임시저장 편지 엔티티
 */
data class DraftLetter(
    val id: Long = 0,
    val receiverName: String,
    val sendDate: String,
    val title: String,
    val content: String,
    val createdAt: Long = System.currentTimeMillis()
)
