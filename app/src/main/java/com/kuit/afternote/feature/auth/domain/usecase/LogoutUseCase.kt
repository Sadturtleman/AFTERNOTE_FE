package com.kuit.afternote.feature.auth.domain.usecase

import com.kuit.afternote.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * 로그아웃 UseCase.
 */
class LogoutUseCase
    @Inject
    constructor(
        private val authRepository: AuthRepository
    ) {
        suspend operator fun invoke(refreshToken: String): Result<Unit> = authRepository.logout(refreshToken)
    }
