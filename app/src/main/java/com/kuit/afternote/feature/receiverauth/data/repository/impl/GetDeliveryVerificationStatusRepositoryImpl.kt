package com.kuit.afternote.feature.receiverauth.data.repository.impl

import com.kuit.afternote.feature.receiverauth.data.repository.iface.ReceiverAuthRepository
import com.kuit.afternote.feature.receiverauth.domain.entity.DeliveryVerificationStatus
import com.kuit.afternote.feature.receiverauth.domain.repository.iface.GetDeliveryVerificationStatusRepository
import javax.inject.Inject

/**
 * 수신자 사망확인 인증 상태 조회 Data Repository 구현체.
 *
 * Data [ReceiverAuthRepository]의 getDeliveryVerificationStatus에 위임하고,
 * DTO를 도메인 [DeliveryVerificationStatus]로 매핑합니다.
 */
class GetDeliveryVerificationStatusRepositoryImpl
    @Inject
    constructor(
        private val receiverAuthRepository: ReceiverAuthRepository
    ) : GetDeliveryVerificationStatusRepository {

    override suspend fun getStatus(authCode: String): Result<DeliveryVerificationStatus> =
        receiverAuthRepository.getDeliveryVerificationStatus(authCode).map { dto ->
            DeliveryVerificationStatus(
                id = dto.id,
                status = dto.status,
                adminNote = dto.adminNote,
                createdAt = dto.createdAt
            )
        }
}
