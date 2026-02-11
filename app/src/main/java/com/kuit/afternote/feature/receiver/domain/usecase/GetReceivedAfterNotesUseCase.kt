package com.kuit.afternote.feature.receiver.domain.usecase

import com.kuit.afternote.feature.receiver.domain.entity.ReceivedAfternote
import com.kuit.afternote.feature.receiver.domain.repository.iface.ReceivedAfternoteRepository
import javax.inject.Inject

/**
 * 수신자에게 전달된 애프터노트 목록 조회 UseCase.
 *
 * GET /api/received/{receiverId}/after-notes API를 호출합니다.
 */
class GetReceivedAfterNotesUseCase
    @Inject
    constructor(
        private val receivedAfternoteRepository: ReceivedAfternoteRepository
    ) {
        suspend operator fun invoke(receiverId: Long): Result<List<ReceivedAfternote>> =
            receivedAfternoteRepository.getReceivedAfterNotes(receiverId = receiverId)
    }
