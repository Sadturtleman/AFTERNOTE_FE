package com.kuit.afternote.feature.receiverauth.data.repository.impl

import com.kuit.afternote.feature.receiver.domain.repository.iface.GetSenderMessageRepository
import com.kuit.afternote.feature.receiverauth.data.repository.iface.ReceiverAuthRepository
import javax.inject.Inject

/**
 * 인증번호 기반 발신자 메시지 조회 Repository 구현.
 *
 * GET /api/receiver-auth/message API 응답의 message 필드를 반환합니다.
 */
class ReceiverAuthMessageRepositoryImpl
    @Inject
    constructor(
        private val receiverAuthRepository: ReceiverAuthRepository
    ) : GetSenderMessageRepository {

    override suspend fun getMessage(authCode: String): Result<String?> =
        receiverAuthRepository.getMessage(authCode).map { it.message }
}
