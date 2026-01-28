package com.kuit.afternote.feature.user.domain.usecase

import com.kuit.afternote.feature.user.domain.model.PushSettings
import com.kuit.afternote.feature.user.domain.repository.UserRepository
import javax.inject.Inject

/**
 * 푸시 알림 설정 수정 UseCase.
 */
class UpdateMyPushSettingsUseCase
    @Inject
    constructor(
        private val userRepository: UserRepository
    ) {
        suspend operator fun invoke(
            userId: Long,
            timeLetter: Boolean?,
            mindRecord: Boolean?,
            afterNote: Boolean?
        ): Result<PushSettings> =
            userRepository.updateMyPushSettings(
                userId = userId,
                timeLetter = timeLetter,
                mindRecord = mindRecord,
                afterNote = afterNote
            )
    }
