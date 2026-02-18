package com.kuit.afternote.feature.receiverauth.data.repository.impl

import com.kuit.afternote.feature.receiver.domain.repository.iface.GetMindRecordDetailByAuthCodeRepository
import com.kuit.afternote.feature.receiverauth.data.repository.iface.ReceiverAuthRepository
import javax.inject.Inject

/**
 * 인증번호 기반 마인드레코드 상세의 대표 이미지 URL 조회 Repository 구현.
 *
 * GET /api/receiver-auth/mind-records/{mindRecordId} (X-Auth-Code) API를 사용합니다.
 */
class ReceiverAuthMindRecordDetailRepositoryImpl
    @Inject
    constructor(
        private val receiverAuthRepository: ReceiverAuthRepository
    ) : GetMindRecordDetailByAuthCodeRepository {

    override suspend fun getMindRecordDetailImageUrl(
        authCode: String,
        mindRecordId: Long
    ): Result<String?> =
        receiverAuthRepository.getMindRecordDetail(
            authCode = authCode,
            mindRecordId = mindRecordId
        ).map { dto ->
            dto.imageList.firstOrNull()?.imageUrl
        }
}
