package com.kuit.afternote.feature.receiver.data.repository.impl

import com.kuit.afternote.feature.receiver.data.mapper.ReceivedMapper
import com.kuit.afternote.feature.receiver.data.repository.iface.ReceivedRepository
import com.kuit.afternote.feature.receiver.domain.entity.ReceivedMindRecord
import com.kuit.afternote.feature.receiver.domain.repository.iface.ReceivedMindRecordRepository
import javax.inject.Inject

/**
 * [ReceivedMindRecordRepository] 구현체.
 *
 * [ReceivedRepository]의 getReceivedMindRecords를 호출하고 DTO를 도메인 엔티티로 변환합니다.
 */
class ReceivedMindRecordRepositoryImpl
    @Inject
    constructor(
        private val receivedRepository: ReceivedRepository
    ) : ReceivedMindRecordRepository {

    override suspend fun getReceivedMindRecords(receiverId: Long): Result<List<ReceivedMindRecord>> =
        receivedRepository.getReceivedMindRecords(receiverId = receiverId)
            .map { response ->
                response.mindRecords.map(ReceivedMapper::toReceivedMindRecord)
            }
}
