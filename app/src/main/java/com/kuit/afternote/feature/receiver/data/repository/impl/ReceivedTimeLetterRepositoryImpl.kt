package com.kuit.afternote.feature.receiver.data.repository.impl

import com.kuit.afternote.feature.receiver.data.mapper.ReceivedMapper
import com.kuit.afternote.feature.receiver.data.repository.iface.ReceivedRepository
import com.kuit.afternote.feature.receiver.domain.entity.ReceivedListWithCount
import com.kuit.afternote.feature.receiver.domain.entity.ReceivedTimeLetter
import com.kuit.afternote.feature.receiver.domain.repository.iface.ReceivedTimeLetterRepository
import javax.inject.Inject

/**
 * [ReceivedTimeLetterRepository] 구현체.
 *
 * [ReceivedRepository]의 getReceivedTimeLetters를 호출하고 DTO를 도메인 엔티티로 변환합니다.
 * API의 totalCount를 그대로 [ReceivedListWithCount.totalCount]에 반영합니다.
 */
class ReceivedTimeLetterRepositoryImpl
    @Inject
    constructor(
        private val receivedRepository: ReceivedRepository
    ) : ReceivedTimeLetterRepository {

    override suspend fun getReceivedTimeLetters(
        receiverId: Long
    ): Result<ReceivedListWithCount<ReceivedTimeLetter>> =
        receivedRepository.getReceivedTimeLetters(receiverId = receiverId)
            .map { response ->
                ReceivedListWithCount(
                    items = response.timeLetters.map(ReceivedMapper::toReceivedTimeLetter),
                    totalCount = response.totalCount
                )
            }

    override suspend fun getReceivedTimeLetterDetail(
        receiverId: Long,
        timeLetterReceiverId: Long
    ): Result<ReceivedTimeLetter> =
        receivedRepository.getReceivedTimeLetterDetail(
            receiverId = receiverId,
            timeLetterReceiverId = timeLetterReceiverId
        ).map(ReceivedMapper::toReceivedTimeLetter)
}
