package com.kuit.afternote.feature.receiver.domain.usecase

import com.kuit.afternote.feature.receiver.domain.repository.iface.ReceiverEmailVerifyRepository
import javax.inject.Inject

/**
 * 수신자 이메일 인증번호 검증 UseCase.
 */
class VerifyReceiverEmailCodeUseCase
    @Inject
    constructor(
        private val repository: ReceiverEmailVerifyRepository
    ) {
    suspend operator fun invoke(email: String, code: String): Result<Unit> =
        repository.verifyEmailCode(email, code)
}
