package com.kuit.afternote.feature.user.domain.usecase

import com.kuit.afternote.feature.user.domain.model.ReceiverAfterNoteSourceItem
import com.kuit.afternote.feature.user.domain.repository.UserRepository
import javax.inject.Inject

/**
 * 수신인별 애프터노트 목록 조회 UseCase.
 * GET /users/receivers/{receiverId}/after-notes
 */
class GetReceiverAfterNotesUseCase
    @Inject
    constructor(
        private val userRepository: UserRepository
    ) {
        suspend operator fun invoke(receiverId: Long): Result<List<ReceiverAfterNoteSourceItem>> =
            userRepository.getReceiverAfterNotes(receiverId = receiverId)
    }
