package com.kuit.afternote.feature.receiverauth.domain.entity

/**
 * 수신자 사망확인 서류 제출·인증 상태 도메인 모델.
 *
 * GET /api/receiver-auth/delivery-verification/status 응답에 대응합니다.
 *
 * @param id 인증 요청 ID
 * @param status 인증 상태 (PENDING, APPROVED, REJECTED)
 * @param adminNote 관리자 메모 (nullable)
 * @param createdAt 생성 일시
 */
data class DeliveryVerificationStatus(
    val id: Long,
    val status: String,
    val adminNote: String? = null,
    val createdAt: String
)
