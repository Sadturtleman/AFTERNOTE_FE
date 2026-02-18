package com.kuit.afternote.feature.receiverauth.domain.repository.iface

import com.kuit.afternote.feature.receiverauth.domain.entity.DeliveryVerificationStatus

/**
 * 수신자 사망확인 인증 상태 조회용 Domain Repository.
 *
 * GET /api/receiver-auth/delivery-verification/status 호출을 래핑합니다.
 */
interface GetDeliveryVerificationStatusRepository {

    /**
     * 수신자가 마지막으로 제출한 인증 요청 상태를 조회합니다.
     *
     * @param authCode 수신자 인증번호 (X-Auth-Code)
     * @return 성공 시 [DeliveryVerificationStatus], 실패 시 [Result.failure]
     */
    suspend fun getStatus(authCode: String): Result<DeliveryVerificationStatus>
}
