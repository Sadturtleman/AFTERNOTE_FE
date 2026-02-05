package com.kuit.afternote.feature.user.domain.usecase

import com.kuit.afternote.feature.user.domain.model.ReceiverListItem
import com.kuit.afternote.feature.user.domain.repository.UserRepository
import javax.inject.Inject

/**
 * 수신인 목록 조회 UseCase.
 * GET /users/receivers
 */
class GetReceiversUseCase
    @Inject
    constructor(
        private val userRepository: UserRepository
    ) {
        suspend operator fun invoke(): Result<List<ReceiverListItem>> =
            userRepository.getReceivers()
    }
