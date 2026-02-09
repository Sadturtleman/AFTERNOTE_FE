package com.kuit.afternote.feature.user.domain.usecase

import com.kuit.afternote.feature.user.domain.model.PushSettings
import com.kuit.afternote.feature.user.domain.repository.UserRepository
import javax.inject.Inject

/**
 * 푸시 알림 설정 조회 UseCase.
 * GET /users/push-settings — 로그인한 사용자의 푸시 알림 수신 설정을 불러옵니다.
 */
class GetMyPushSettingsUseCase
    @Inject
    constructor(
        private val userRepository: UserRepository
    ) {
        /**
         * @param userId 사용자 ID (query, required)
         * @return [PushSettings] (timeLetter, mindRecord, afterNote)
         */
        suspend operator fun invoke(userId: Long): Result<PushSettings> =
            userRepository.getMyPushSettings(userId = userId)
    }
