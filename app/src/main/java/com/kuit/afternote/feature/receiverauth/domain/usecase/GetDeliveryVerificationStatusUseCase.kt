package com.kuit.afternote.feature.receiverauth.domain.usecase

import com.kuit.afternote.feature.receiverauth.domain.entity.DeliveryVerificationStatus
import com.kuit.afternote.feature.receiverauth.domain.repository.iface.GetDeliveryVerificationStatusRepository
import javax.inject.Inject

/**
 * 수신자가 마지막으로 제출한 사망확인 인증 요청 상태를 조회하는 UseCase.
 */
class GetDeliveryVerificationStatusUseCase
    @Inject
    constructor(
        private val getDeliveryVerificationStatusRepository: GetDeliveryVerificationStatusRepository
    ) {

    /**
     * 인증 요청 상태를 조회합니다.
     *
     * @param authCode 수신자 인증번호 (X-Auth-Code)
     * @return 성공 시 [DeliveryVerificationStatus], 실패 시 [Result.failure]
     */
    suspend operator fun invoke(authCode: String): Result<DeliveryVerificationStatus> =
        getDeliveryVerificationStatusRepository.getStatus(authCode)
}
