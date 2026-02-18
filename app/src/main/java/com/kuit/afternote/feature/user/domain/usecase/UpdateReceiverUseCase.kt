package com.kuit.afternote.feature.user.domain.usecase

import com.kuit.afternote.feature.user.domain.repository.UserRepository
import javax.inject.Inject

/**
 * 수신자 수정 UseCase.
 * PATCH /users/receivers/{receiverId}
 */
class UpdateReceiverUseCase
    @Inject
    constructor(
        private val userRepository: UserRepository
    ) {
        suspend operator fun invoke(
            receiverId: Long,
            name: String,
            relation: String,
            phone: String? = null,
            email: String? = null
        ): Result<Unit> =
            userRepository.updateReceiver(
                receiverId = receiverId,
                name = name,
                relation = relation,
                phone = phone,
                email = email
            )
    }
