package com.kuit.afternote.feature.receiverauth.data.repository.impl

import com.kuit.afternote.feature.receiver.domain.entity.ReceivedAfternoteDetail
import com.kuit.afternote.feature.receiver.domain.repository.iface.GetAfternoteDetailByAuthCodeRepository
import com.kuit.afternote.feature.receiverauth.data.mapper.toReceivedAfternoteDetail
import com.kuit.afternote.feature.receiverauth.data.repository.iface.ReceiverAuthRepository
import javax.inject.Inject

/**
 * 인증번호 기반 애프터노트 상세 조회 Repository 구현.
 *
 * receiver-auth API 응답을 receiver 도메인 엔티티로 매핑하여 반환합니다.
 */
class ReceiverAuthAfternoteDetailRepositoryImpl
    @Inject
    constructor(
        private val receiverAuthRepository: ReceiverAuthRepository
    ) : GetAfternoteDetailByAuthCodeRepository {

    override suspend fun getAfternoteDetail(
        authCode: String,
        afternoteId: Long
    ): Result<ReceivedAfternoteDetail> =
        receiverAuthRepository.getAfternoteDetail(
            authCode = authCode,
            afternoteId = afternoteId
        ).map { it.toReceivedAfternoteDetail() }
}
