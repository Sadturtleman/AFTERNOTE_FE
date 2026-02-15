package com.kuit.afternote.feature.receiver.domain.usecase

import com.kuit.afternote.feature.receiver.domain.entity.ReceivedMindRecord
import com.kuit.afternote.feature.receiver.domain.repository.iface.ReceivedMindRecordRepository
import javax.inject.Inject

/**
 * 수신자에게 공유된 마인드레코드 목록 조회 UseCase.
 *
 * GET /api/received/{receiverId}/mind-records API를 호출합니다.
 */
class GetReceivedMindRecordsUseCase
    @Inject
    constructor(
        private val receivedMindRecordRepository: ReceivedMindRecordRepository
    ) {
    suspend operator fun invoke(receiverId: Long): Result<List<ReceivedMindRecord>> =
        receivedMindRecordRepository.getReceivedMindRecords(receiverId = receiverId)
}
