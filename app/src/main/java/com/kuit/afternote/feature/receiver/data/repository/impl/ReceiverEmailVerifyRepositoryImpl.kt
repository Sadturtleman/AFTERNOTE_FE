package com.kuit.afternote.feature.receiver.data.repository.impl

import com.kuit.afternote.data.remote.requireSuccess
import com.kuit.afternote.feature.auth.data.api.AuthApiService
import com.kuit.afternote.feature.auth.data.dto.SendEmailCodeRequest
import com.kuit.afternote.feature.auth.data.dto.VerifyEmailRequest
import com.kuit.afternote.feature.receiver.domain.repository.iface.ReceiverEmailVerifyRepository
import javax.inject.Inject

/**
 * [ReceiverEmailVerifyRepository] 구현체.
 *
 * 수신자 이메일 인증은 Auth API (POST /auth/email/send, POST /auth/email/verify)를 사용합니다.
 */
class ReceiverEmailVerifyRepositoryImpl
    @Inject
    constructor(
        private val authApi: AuthApiService
    ) : ReceiverEmailVerifyRepository {

    override suspend fun sendEmailCode(email: String): Result<Unit> =
        runCatching {
            authApi.sendEmailCode(SendEmailCodeRequest(email)).requireSuccess()
        }

    override suspend fun verifyEmailCode(email: String, code: String): Result<Unit> =
        runCatching {
            authApi.verifyEmail(VerifyEmailRequest(email, code)).requireSuccess()
        }
}
