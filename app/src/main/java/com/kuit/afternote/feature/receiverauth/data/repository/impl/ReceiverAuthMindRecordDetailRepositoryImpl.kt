package com.kuit.afternote.feature.receiverauth.data.repository.impl

import com.kuit.afternote.feature.receiver.domain.entity.ReceivedMindRecordDetail
import com.kuit.afternote.feature.receiver.domain.repository.iface.GetMindRecordDetailByAuthCodeRepository
import com.kuit.afternote.feature.receiverauth.data.repository.iface.ReceiverAuthRepository
import javax.inject.Inject

/**
 * 인증번호 기반 마인드레코드 상세 조회 Repository 구현.
 *
 * GET /api/receiver-auth/mind-records/{mindRecordId} (X-Auth-Code) API를 사용합니다.
 */
class ReceiverAuthMindRecordDetailRepositoryImpl
    @Inject
    constructor(
        private val receiverAuthRepository: ReceiverAuthRepository
    ) : GetMindRecordDetailByAuthCodeRepository {

    override suspend fun getMindRecordDetail(
        authCode: String,
        mindRecordId: Long
    ): Result<ReceivedMindRecordDetail> =
        receiverAuthRepository.getMindRecordDetail(
            authCode = authCode,
            mindRecordId = mindRecordId
        ).map { dto ->
            ReceivedMindRecordDetail(
                mindRecordId = dto.id,
                title = dto.title,
                recordDate = dto.recordDate,
                content = dto.content,
                questionContent = dto.questionContent,
                category = dto.category,
                imageUrls = dto.imageList.map { it.imageUrl }
            )
        }
}
