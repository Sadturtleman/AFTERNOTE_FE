package com.kuit.afternote.feature.receiver.domain.usecase

import com.kuit.afternote.feature.receiver.domain.entity.ReceivedListWithCount
import com.kuit.afternote.feature.receiver.domain.entity.ReceivedTimeLetter
import com.kuit.afternote.feature.receiver.domain.repository.iface.ReceivedTimeLetterRepository
import javax.inject.Inject

/**
 * 수신자에게 배달된 타임레터 목록·전체 개수 조회 UseCase.
 *
 * GET /api/received/{receiverId}/time-letters API를 호출합니다.
 */
class GetReceivedTimeLettersUseCase
    @Inject
    constructor(
        private val receivedTimeLetterRepository: ReceivedTimeLetterRepository
    ) {
        suspend operator fun invoke(
            receiverId: Long
        ): Result<ReceivedListWithCount<ReceivedTimeLetter>> =
            receivedTimeLetterRepository.getReceivedTimeLetters(receiverId = receiverId)
    }
