package com.kuit.afternote.feature.admin.data.dto

import kotlinx.serialization.Serializable

/**
 * 관리자용 인증 요청 거절 API 요청 body.
 *
 * POST /api/admin/verifications/{id}/reject 시 선택적으로 adminNote를 전달합니다.
 */
@Serializable
data class RejectVerificationRequestDto(
    val adminNote: String? = null
)

/**
 * 관리자용 인증 요청 승인 API 요청 body (AdminVerificationActionRequest).
 *
 * POST /api/admin/verifications/{id}/approve 시 선택적으로 adminNote를 전달합니다.
 */
@Serializable
data class ApproveVerificationRequestDto(
    val adminNote: String? = null
)

/**
 * 관리자용 인증 요청 응답 DTO.
 *
 * 사망확인 서류 인증 요청 조회/승인/거절 API의 data 필드 스키마.
 *
 * @param status 사망확인 서류 인증 상태 (PENDING, APPROVED, REJECTED)
 * @param adminNote 관리자 메모 (nullable)
 */
@Serializable
data class AdminVerificationResponseDto(
    val id: Long,
    val userId: Long,
    val senderName: String,
    val senderEmail: String,
    val receiverId: Long,
    val receiverName: String,
    val status: String,
    val deathCertificateUrl: String,
    val familyRelationCertificateUrl: String,
    val adminNote: String? = null,
    val createdAt: String
)
