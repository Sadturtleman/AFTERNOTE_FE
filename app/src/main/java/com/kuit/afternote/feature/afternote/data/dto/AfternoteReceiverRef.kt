package com.kuit.afternote.feature.afternote.data.dto

import kotlinx.serialization.Serializable

/**
 * 수신자 참조 (API ReceiverRequest). 수신자 목록은 모든 카테고리에서 사용 가능.
 */
@Serializable
data class AfternoteReceiverRef(
    val receiverId: Long,
)
