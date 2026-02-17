package com.kuit.afternote.feature.admin.data.api

import com.kuit.afternote.data.remote.ApiResponse
import com.kuit.afternote.feature.admin.data.dto.AdminVerificationResponseDto
import com.kuit.afternote.feature.admin.data.dto.ApproveVerificationRequestDto
import com.kuit.afternote.feature.admin.data.dto.RejectVerificationRequestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * 관리자 API 서비스. (스웨거 기준)
 *
 * - GET /api/admin/verifications: 대기 중인 인증 요청 목록 조회
 * - GET /api/admin/verifications/{id}: 인증 요청 상세 조회
 * - POST /api/admin/verifications/{id}/approve: 인증 요청 승인
 * - POST /api/admin/verifications/{id}/reject: 인증 요청 거절
 */
interface AdminApiService {

    /**
     * 대기 중인(PENDING) 사망확인 인증 요청 목록을 조회합니다.
     *
     * 관리자 권한(ADMIN) 필요. 비인증 시 401, 권한 없으면 403.
     *
     * @return data: 인증 요청 목록 (발신자/수신자 정보, Presigned URL, 제출 일시, 관리자 메모)
     */
    @GET("api/admin/verifications")
    suspend fun getPendingVerifications(): ApiResponse<List<AdminVerificationResponseDto>?>

    /**
     * 관리자가 특정 인증 요청의 상세 정보를 조회합니다.
     *
     * Presigned URL이 포함되어 있으므로, URL 만료 전에 서류를 확인하세요.
     * 관리자 권한(ADMIN) 필요. 비인증 시 401, 권한 없으면 403, 미존재 시 404.
     *
     * @param id 인증 요청 ID (path)
     * @return data: 인증 요청 상세 (발신자/수신자 정보, Presigned URL, 상태, 관리자 메모, 생성일시)
     */
    @GET("api/admin/verifications/{id}")
    suspend fun getVerificationDetail(@Path("id") id: Long): ApiResponse<AdminVerificationResponseDto?>

    /**
     * 관리자가 사망확인 인증 요청을 승인합니다.
     *
     * 승인 시 인증 상태가 APPROVED로 변경되고, 발신자의 conditionFulfilled가 true로 변경되어
     * 수신자가 콘텐츠를 열람할 수 있게 됩니다. adminNote(선택)로 메모를 남길 수 있습니다.
     * 이미 처리된(APPROVED/REJECTED) 요청은 재처리할 수 없습니다.
     *
     * @param id 인증 요청 ID (path)
     * @param body adminNote (선택)
     * @return 변경된 인증 요청 정보
     */
    @POST("api/admin/verifications/{id}/approve")
    suspend fun approveVerification(
        @Path("id") id: Long,
        @Body body: ApproveVerificationRequestDto
    ): ApiResponse<AdminVerificationResponseDto?>

    /**
     * 관리자가 사망확인 인증 요청을 거절합니다.
     *
     * 거절 시 인증 상태가 REJECTED로 변경되고, 수신자는 서류를 다시 제출할 수 있습니다.
     * adminNote(선택)로 거절 사유를 남길 수 있습니다.
     * 이미 처리된(APPROVED/REJECTED) 요청은 재처리할 수 없습니다.
     *
     * @param id 인증 요청 ID (path)
     * @param body adminNote (선택)
     * @return 변경된 인증 요청 정보
     */
    @POST("api/admin/verifications/{id}/reject")
    suspend fun rejectVerification(
        @Path("id") id: Long,
        @Body body: RejectVerificationRequestDto
    ): ApiResponse<AdminVerificationResponseDto?>
}
