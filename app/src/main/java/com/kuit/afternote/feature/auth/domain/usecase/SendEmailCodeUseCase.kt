package com.kuit.afternote.feature.auth.domain.usecase

import com.kuit.afternote.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * 이메일 인증번호 발송 UseCase.
 */
class SendEmailCodeUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(email: String): Result<Unit> =
        authRepository.sendEmailCode(email)
}
