package com.kuit.afternote.feature.receiverauth.domain.usecase

import com.kuit.afternote.feature.receiverauth.domain.entity.ReceiverAuthVerifyResult
import com.kuit.afternote.feature.receiverauth.domain.repository.iface.ReceiverAuthRepository
import javax.inject.Inject

/**
 * 수신자 인증번호(마스터 키) 검증 UseCase.
 *
 * 마스터 키 입력 후 API 검증을 수행하고, 성공 시 수신자 정보를 반환합니다.
 */
class VerifyReceiverAuthUseCase
    @Inject
    constructor(
        private val receiverAuthRepository: ReceiverAuthRepository
    ) {

    /**
     * 인증번호를 검증합니다.
     *
     * @param authCode 수신자 인증번호 (마스터 키)
     * @return 성공 시 [ReceiverAuthVerifyResult], 400 등 실패 시 [Result.failure]
     */
    suspend operator fun invoke(authCode: String): Result<ReceiverAuthVerifyResult> =
        receiverAuthRepository.verify(authCode)
}
