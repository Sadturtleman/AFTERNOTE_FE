package com.kuit.afternote.feature.receiverauth.domain.repository.iface

import com.kuit.afternote.feature.receiverauth.domain.entity.ReceiverAuthVerifyResult

/**
 * 수신자 인증번호 검증용 Domain Repository 인터페이스.
 *
 * 인증번호 검증(verify) 결과를 도메인 모델로 반환합니다.
 */
interface ReceiverAuthRepository {

    /**
     * 수신자 인증번호를 검증하고 수신자/발신자 정보를 반환합니다.
     *
     * @param authCode 수신자 인증번호 (마스터 키)
     * @return 성공 시 [ReceiverAuthVerifyResult], 실패 시 [Result.failure]
     */
    suspend fun verify(authCode: String): Result<ReceiverAuthVerifyResult>
}
