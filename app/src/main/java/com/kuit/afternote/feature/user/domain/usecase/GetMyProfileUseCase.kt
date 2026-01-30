package com.kuit.afternote.feature.user.domain.usecase

import com.kuit.afternote.feature.user.domain.model.UserProfile
import com.kuit.afternote.feature.user.domain.repository.UserRepository
import javax.inject.Inject

/**
 * 내 프로필 조회 UseCase.
 */
class GetMyProfileUseCase
    @Inject
    constructor(
        private val userRepository: UserRepository
    ) {
        suspend operator fun invoke(userId: Long): Result<UserProfile> = userRepository.getMyProfile(userId = userId)
    }
