package com.kuit.afternote.feature.afternote.domain.usecase

import com.kuit.afternote.feature.afternote.domain.model.CreateSocialInput
import com.kuit.afternote.feature.afternote.domain.repository.AfternoteRepository
import javax.inject.Inject

/**
 * 소셜 네트워크 애프터노트 생성 UseCase.
 *
 * POST /api/afternotes (category = SOCIAL)
 */
class CreateSocialAfternoteUseCase
    @Inject
    constructor(
        private val repository: AfternoteRepository,
    ) {
        suspend operator fun invoke(input: CreateSocialInput): Result<Long> =
            repository.createSocial(
                input = input,
            )
    }
