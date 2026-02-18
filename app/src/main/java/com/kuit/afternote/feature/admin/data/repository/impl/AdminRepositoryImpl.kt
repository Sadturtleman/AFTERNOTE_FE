package com.kuit.afternote.feature.admin.data.repository.impl

import com.kuit.afternote.data.remote.requireData
import com.kuit.afternote.feature.admin.data.api.AdminApiService
import com.kuit.afternote.feature.admin.data.dto.AdminVerificationResponseDto
import com.kuit.afternote.feature.admin.data.dto.ApproveVerificationRequestDto
import com.kuit.afternote.feature.admin.data.dto.RejectVerificationRequestDto
import com.kuit.afternote.feature.admin.data.repository.iface.AdminRepository
import javax.inject.Inject

/**
 * 관리자 API용 Data Repository 구현체.
 */
class AdminRepositoryImpl
    @Inject
    constructor(
        private val api: AdminApiService
    ) : AdminRepository {

    override suspend fun getPendingVerifications(): Result<List<AdminVerificationResponseDto>> =
        runCatching {
            api.getPendingVerifications().requireData() ?: emptyList()
        }

    override suspend fun getVerificationDetail(id: Long): Result<AdminVerificationResponseDto> =
        runCatching {
            api.getVerificationDetail(id).requireData()
        }

    override suspend fun approveVerification(
        id: Long,
        request: ApproveVerificationRequestDto
    ): Result<AdminVerificationResponseDto> =
        runCatching {
            api.approveVerification(id = id, body = request).requireData()
        }

    override suspend fun rejectVerification(
        id: Long,
        request: RejectVerificationRequestDto
    ): Result<AdminVerificationResponseDto> =
        runCatching {
            api.rejectVerification(id = id, body = request).requireData()
        }
}
