package com.kuit.afternote.feature.afternote.domain.usecase

import com.kuit.afternote.feature.afternote.domain.repository.iface.AfternoteRepository
import javax.inject.Inject

/**
 * 소셜 네트워크 애프터노트 생성 UseCase.
 *
 * POST /api/afternotes (category = SOCIAL)
 */
class CreateSocialAfternoteUseCase
    @Inject
    constructor(
        private val repository: AfternoteRepository
    ) {
        suspend operator fun invoke(
            title: String,
            processMethod: String,
            actions: List<String>,
            leaveMessage: String? = null,
            credentialsId: String? = null,
            credentialsPassword: String? = null
        ): Result<Long> = repository.createSocial(
            title = title,
            processMethod = processMethod,
            actions = actions,
            leaveMessage = leaveMessage,
            credentialsId = credentialsId,
            credentialsPassword = credentialsPassword
        )
    }
