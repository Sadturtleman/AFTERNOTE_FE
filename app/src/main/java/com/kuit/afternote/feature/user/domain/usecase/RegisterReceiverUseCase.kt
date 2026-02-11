package com.kuit.afternote.feature.user.domain.usecase

import com.kuit.afternote.feature.user.domain.repository.UserRepository
import javax.inject.Inject

/**
 * 수신자 등록 UseCase.
 * POST /users/receivers
 */
class RegisterReceiverUseCase
    @Inject
    constructor(
        private val userRepository: UserRepository
    ) {
        suspend operator fun invoke(
            name: String,
            relation: String,
            phone: String? = null,
            email: String? = null
        ): Result<Long> =
            userRepository.registerReceiver(
                name = name,
                relation = relation,
                phone = phone,
                email = email
            )
    }
