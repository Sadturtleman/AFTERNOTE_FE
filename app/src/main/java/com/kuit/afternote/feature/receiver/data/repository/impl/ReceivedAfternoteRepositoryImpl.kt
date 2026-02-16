package com.kuit.afternote.feature.receiver.data.repository.impl

import com.kuit.afternote.feature.receiver.data.mapper.ReceivedMapper
import com.kuit.afternote.feature.receiver.data.repository.iface.ReceivedRepository
import com.kuit.afternote.feature.receiver.domain.entity.ReceivedAfternote
import com.kuit.afternote.feature.receiver.domain.entity.ReceivedListWithCount
import com.kuit.afternote.feature.receiver.domain.repository.iface.ReceivedAfternoteRepository
import javax.inject.Inject

/**
 * [ReceivedAfternoteRepository] 구현체.
 *
 * [ReceivedRepository]의 getReceivedAfterNotes를 호출하고 DTO를 도메인 엔티티로 변환합니다.
 * API의 totalCount를 그대로 [ReceivedListWithCount.totalCount]에 반영합니다.
 */
class ReceivedAfternoteRepositoryImpl
    @Inject
    constructor(
        private val receivedRepository: ReceivedRepository
    ) : ReceivedAfternoteRepository {

    override suspend fun getReceivedAfterNotes(
        receiverId: Long
    ): Result<ReceivedListWithCount<ReceivedAfternote>> =
        receivedRepository.getReceivedAfterNotes(receiverId = receiverId)
            .map { response ->
                ReceivedListWithCount(
                    items = response.afternotes.map(ReceivedMapper::toReceivedAfternote),
                    totalCount = response.totalCount
                )
            }
}
