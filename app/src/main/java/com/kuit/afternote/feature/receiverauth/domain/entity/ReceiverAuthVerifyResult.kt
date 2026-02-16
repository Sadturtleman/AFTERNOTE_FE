package com.kuit.afternote.feature.receiverauth.domain.entity

/**
 * 수신자 인증번호 검증 성공 시 반환되는 도메인 모델.
 *
 * @param receiverId 수신자 ID
 * @param receiverName 수신자 이름 (nullable)
 * @param senderName 발신자 이름 (nullable)
 * @param relation 관계 (nullable)
 */
data class ReceiverAuthVerifyResult(
    val receiverId: Long,
    val receiverName: String? = null,
    val senderName: String? = null,
    val relation: String? = null
)
