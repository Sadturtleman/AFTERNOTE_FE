package com.kuit.afternote.feature.auth.domain.usecase

import com.kuit.afternote.feature.auth.domain.model.SignUpResult
import com.kuit.afternote.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * 회원가입 UseCase.
 */
class SignUpUseCase
    @Inject
    constructor(
        private val authRepository: AuthRepository
    ) {
        suspend operator fun invoke(
            email: String,
            password: String,
            name: String,
            profileUrl: String?
        ): Result<SignUpResult> = authRepository.signUp(email, password, name, profileUrl)
    }
