package com.kuit.afternote.feature.user.domain.usecase

import com.kuit.afternote.feature.user.domain.model.ReceiverTimeLetterItem
import com.kuit.afternote.feature.user.domain.repository.UserRepository
import javax.inject.Inject

/**
 * 수신인별 타임레터 목록 조회 UseCase.
 * GET /users/receivers/{receiverId}/time-letters
 */
class GetReceiverTimeLettersUseCase
    @Inject
    constructor(
        private val userRepository: UserRepository
    ) {
        suspend operator fun invoke(receiverId: Long): Result<List<ReceiverTimeLetterItem>> =
            userRepository.getReceiverTimeLetters(receiverId = receiverId)
    }
