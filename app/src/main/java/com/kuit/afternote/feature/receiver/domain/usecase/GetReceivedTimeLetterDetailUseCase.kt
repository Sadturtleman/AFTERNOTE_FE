package com.kuit.afternote.feature.receiver.domain.usecase

import com.kuit.afternote.feature.receiver.domain.entity.ReceivedTimeLetter
import com.kuit.afternote.feature.receiver.domain.repository.iface.ReceivedTimeLetterRepository
import javax.inject.Inject

/**
 * 수신한 타임레터 상세 조회 + 읽음 처리 UseCase.
 *
 * GET /api/received/{receiverId}/time-letters/{timeLetterReceiverId} API를 호출합니다.
 */
class GetReceivedTimeLetterDetailUseCase
    @Inject
    constructor(
        private val receivedTimeLetterRepository: ReceivedTimeLetterRepository
    ) {
        suspend operator fun invoke(
            receiverId: Long,
            timeLetterReceiverId: Long
        ): Result<ReceivedTimeLetter> =
            receivedTimeLetterRepository.getReceivedTimeLetterDetail(
                receiverId = receiverId,
                timeLetterReceiverId = timeLetterReceiverId
            )
    }
