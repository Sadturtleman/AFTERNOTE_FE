package com.kuit.afternote.feature.auth.domain.usecase

import com.kuit.afternote.feature.auth.domain.model.ReissueResult
import com.kuit.afternote.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * 토큰 재발급 UseCase.
 */
class ReissueUseCase
    @Inject
    constructor(
        private val authRepository: AuthRepository
    ) {
        suspend operator fun invoke(refreshToken: String): Result<ReissueResult> = authRepository.reissue(refreshToken)
    }
