package com.kuit.afternote.feature.admin.data.repository.iface

import com.kuit.afternote.feature.admin.data.dto.AdminVerificationResponseDto
import com.kuit.afternote.feature.admin.data.dto.ApproveVerificationRequestDto
import com.kuit.afternote.feature.admin.data.dto.RejectVerificationRequestDto

/**
 * 관리자 API용 Data Repository 인터페이스.
 *
 * 대기 중인 인증 요청 목록 조회, 인증 요청 승인/거절 등 관리자 전용 API를 호출합니다.
 */
interface AdminRepository {

    /**
     * 대기 중인(PENDING) 사망확인 인증 요청 목록을 조회합니다.
     *
     * @return 성공 시 인증 요청 목록, 실패 시 Result.failure
     */
    suspend fun getPendingVerifications(): Result<List<AdminVerificationResponseDto>>

    /**
     * 관리자가 특정 인증 요청의 상세 정보를 조회합니다.
     *
     * @param id 인증 요청 ID
     * @return 성공 시 인증 요청 상세, 실패 시 Result.failure
     */
    suspend fun getVerificationDetail(id: Long): Result<AdminVerificationResponseDto>

    /**
     * 관리자가 사망확인 인증 요청을 승인합니다.
     *
     * @param id 인증 요청 ID
     * @param request adminNote (선택)
     * @return 성공 시 변경된 인증 요청 정보, 실패 시 Result.failure
     */
    suspend fun approveVerification(
        id: Long,
        request: ApproveVerificationRequestDto
    ): Result<AdminVerificationResponseDto>

    /**
     * 관리자가 사망확인 인증 요청을 거절합니다.
     *
     * @param id 인증 요청 ID
     * @param request adminNote (선택)
     * @return 성공 시 변경된 인증 요청 정보, 실패 시 Result.failure
     */
    suspend fun rejectVerification(
        id: Long,
        request: RejectVerificationRequestDto
    ): Result<AdminVerificationResponseDto>
}
