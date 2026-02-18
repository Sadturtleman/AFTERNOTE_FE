package com.kuit.afternote.feature.receiver.domain.usecase

import com.kuit.afternote.feature.receiver.domain.repository.iface.GetSenderMessageRepository
import javax.inject.Inject

/**
 * 수신자 인증번호(authCode)로 발신자 메시지 조회 UseCase.
 *
 * GET /api/receiver-auth/message (X-Auth-Code) API를 사용합니다.
 */
class GetSenderMessageUseCase
    @Inject
    constructor(
        private val getSenderMessageRepository: GetSenderMessageRepository
    ) {
    suspend operator fun invoke(authCode: String): Result<String?> =
        getSenderMessageRepository.getMessage(authCode)
}
