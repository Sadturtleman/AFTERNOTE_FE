package com.kuit.afternote.feature.user.domain.usecase

import com.kuit.afternote.feature.user.domain.model.PushSettings
import com.kuit.afternote.feature.user.domain.repository.UserRepository
import javax.inject.Inject

/**
 * 푸시 알림 설정 조회 UseCase.
 */
class GetMyPushSettingsUseCase
    @Inject
    constructor(
        private val userRepository: UserRepository
    ) {
        suspend operator fun invoke(userId: Long): Result<PushSettings> = userRepository.getMyPushSettings(userId = userId)
    }
