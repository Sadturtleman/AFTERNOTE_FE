package com.kuit.afternote.feature.receiver.domain.usecase

import com.kuit.afternote.feature.receiver.domain.repository.iface.ReceiverEmailVerifyRepository
import javax.inject.Inject

/**
 * 수신자 이메일 인증번호 발송 UseCase.
 */
class SendReceiverEmailCodeUseCase
    @Inject
    constructor(
        private val repository: ReceiverEmailVerifyRepository
    ) {
    suspend operator fun invoke(email: String): Result<Unit> = repository.sendEmailCode(email)
}
