package com.kuit.afternote.feature.auth.domain.usecase

import com.kuit.afternote.feature.auth.domain.model.EmailVerifyResult
import com.kuit.afternote.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * 이메일 인증번호 확인 UseCase.
 */
class VerifyEmailUseCase
    @Inject
    constructor(
        private val authRepository: AuthRepository
    ) {
        suspend operator fun invoke(
            email: String,
            certificateCode: String
        ): Result<EmailVerifyResult> = authRepository.verifyEmail(email, certificateCode)
    }
