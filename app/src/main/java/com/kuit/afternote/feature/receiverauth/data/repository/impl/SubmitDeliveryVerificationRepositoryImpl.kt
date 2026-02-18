package com.kuit.afternote.feature.receiverauth.data.repository.impl

import com.kuit.afternote.feature.receiverauth.data.dto.DeliveryVerificationRequestDto
import com.kuit.afternote.feature.receiverauth.data.repository.iface.ReceiverAuthRepository
import com.kuit.afternote.feature.receiverauth.domain.repository.iface.SubmitDeliveryVerificationRepository
import javax.inject.Inject

/**
 * 수신자 사망확인 서류 제출 Data Repository 구현체.
 *
 * Data [ReceiverAuthRepository]의 submitDeliveryVerification에 위임합니다.
 */
class SubmitDeliveryVerificationRepositoryImpl
    @Inject
    constructor(
        private val receiverAuthRepository: ReceiverAuthRepository
    ) : SubmitDeliveryVerificationRepository {

    override suspend fun submit(
        authCode: String,
        deathCertificateUrl: String,
        familyRelationCertificateUrl: String
    ): Result<Unit> =
        receiverAuthRepository.submitDeliveryVerification(
            authCode = authCode,
            request = DeliveryVerificationRequestDto(
                deathCertificateUrl = deathCertificateUrl,
                familyRelationCertificateUrl = familyRelationCertificateUrl
            )
        ).map { }
}
