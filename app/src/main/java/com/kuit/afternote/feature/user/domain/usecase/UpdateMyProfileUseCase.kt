package com.kuit.afternote.feature.user.domain.usecase

import com.kuit.afternote.feature.user.domain.model.UserProfile
import com.kuit.afternote.feature.user.domain.repository.UserRepository
import javax.inject.Inject

/**
 * 프로필 수정 UseCase.
 */
class UpdateMyProfileUseCase
    @Inject
    constructor(
        private val userRepository: UserRepository
    ) {
        suspend operator fun invoke(
            userId: Long,
            name: String?,
            phone: String?,
            profileImageUrl: String?
        ): Result<UserProfile> =
            userRepository.updateMyProfile(
                userId = userId,
                name = name,
                phone = phone,
                profileImageUrl = profileImageUrl
            )
    }
