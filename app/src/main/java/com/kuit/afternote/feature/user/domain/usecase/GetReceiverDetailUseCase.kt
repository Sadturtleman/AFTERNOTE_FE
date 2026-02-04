package com.kuit.afternote.feature.user.domain.usecase

import com.kuit.afternote.feature.user.domain.model.ReceiverDetail
import com.kuit.afternote.feature.user.domain.repository.UserRepository
import javax.inject.Inject

/**
 * 수신인 상세 조회 UseCase.
 * GET /users/receivers/{receiverId}
 */
class GetReceiverDetailUseCase
    @Inject
    constructor(
        private val userRepository: UserRepository
    ) {
        suspend operator fun invoke(receiverId: Long): Result<ReceiverDetail> =
            userRepository.getReceiverDetail(receiverId = receiverId)
    }
