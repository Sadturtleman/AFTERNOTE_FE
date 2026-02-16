package com.kuit.afternote.feature.receiverauth.data.repository.impl

import com.kuit.afternote.feature.receiver.domain.entity.ReceivedListWithCount
import com.kuit.afternote.feature.receiver.domain.entity.ReceivedMindRecord
import com.kuit.afternote.feature.receiver.domain.repository.iface.GetMindRecordsByAuthCodeRepository
import com.kuit.afternote.feature.receiverauth.data.mapper.toReceivedMindRecord
import com.kuit.afternote.feature.receiverauth.data.repository.iface.ReceiverAuthRepository
import javax.inject.Inject

/**
 * 인증번호 기반 마인드레코드 목록 조회 Repository 구현.
 *
 * receiver-auth API 응답을 receiver 도메인 엔티티로 매핑하여 반환합니다.
 */
class ReceiverAuthMindRecordRepositoryImpl
    @Inject
    constructor(
        private val receiverAuthRepository: ReceiverAuthRepository
    ) : GetMindRecordsByAuthCodeRepository {

    override suspend fun getMindRecords(authCode: String): Result<ReceivedListWithCount<ReceivedMindRecord>> =
        receiverAuthRepository.getMindRecords(authCode).map { dto ->
            ReceivedListWithCount(
                items = dto.mindRecords.map { it.toReceivedMindRecord() },
                totalCount = dto.totalCount
            )
        }
}
