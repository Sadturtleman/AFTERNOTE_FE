package com.kuit.afternote.feature.auth.domain.usecase

import com.kuit.afternote.feature.auth.domain.model.LoginResult
import com.kuit.afternote.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * 로그인 UseCase.
 */
class LoginUseCase
    @Inject
    constructor(
        private val authRepository: AuthRepository
    ) {
        suspend operator fun invoke(
            email: String,
            password: String
        ): Result<LoginResult> = authRepository.login(email, password)
    }
