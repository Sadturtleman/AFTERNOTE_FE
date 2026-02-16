package com.kuit.afternote.feature.receiver.data.repository.impl

import com.kuit.afternote.data.remote.requireData
import com.kuit.afternote.feature.receiver.data.api.ReceivedApiService
import com.kuit.afternote.feature.receiver.data.dto.CreateMindRecordReceiverRequestDto
import com.kuit.afternote.feature.receiver.data.dto.CreateTimeLetterReceiverRequestDto
import com.kuit.afternote.feature.receiver.data.dto.ReceivedAfternoteListResponseDto
import com.kuit.afternote.feature.receiver.data.dto.ReceivedMindRecordListResponseDto
import com.kuit.afternote.feature.receiver.data.dto.ReceivedTimeLetterListResponseDto
import com.kuit.afternote.feature.receiver.data.dto.ReceivedTimeLetterResponseDto
import com.kuit.afternote.feature.receiver.data.repository.iface.ReceivedRepository
import javax.inject.Inject

/**
 * [ReceivedRepository] 구현체. (스웨거 기준)
 *
 * ReceivedApiService를 통해 타임레터/마인드레코드 수신자 등록 및 수신 목록 조회를 수행합니다.
 */
class ReceivedRepositoryImpl
    @Inject
    constructor(
        private val api: ReceivedApiService
    ) : ReceivedRepository {

    override suspend fun registerTimeLetterReceivers(
        timeLetterId: Long,
        receiverIds: List<Long>,
        deliveredAt: String?
    ): Result<List<Long>> =
        runCatching {
            val response = api.registerTimeLetterReceivers(
                CreateTimeLetterReceiverRequestDto(
                    timeLetterId = timeLetterId,
                    receiverIds = receiverIds,
                    deliveredAt = deliveredAt
                )
            )
            response.requireData()
        }

    override suspend fun registerMindRecordReceivers(
        mindRecordId: Long,
        receiverIds: List<Long>
    ): Result<List<Long>> =
        runCatching {
            val response = api.registerMindRecordReceivers(
                CreateMindRecordReceiverRequestDto(
                    mindRecordId = mindRecordId,
                    receiverIds = receiverIds
                )
            )
            response.requireData()
        }

    override suspend fun getReceivedTimeLetters(receiverId: Long): Result<ReceivedTimeLetterListResponseDto> =
        runCatching {
            api.getReceivedTimeLetters(receiverId = receiverId).requireData()
        }

    override suspend fun getReceivedTimeLetterDetail(
        receiverId: Long,
        timeLetterReceiverId: Long
    ): Result<ReceivedTimeLetterResponseDto> =
        runCatching {
            api.getReceivedTimeLetterDetail(
                receiverId = receiverId,
                timeLetterReceiverId = timeLetterReceiverId
            ).requireData()
        }

    override suspend fun getReceivedMindRecords(receiverId: Long): Result<ReceivedMindRecordListResponseDto> =
        runCatching {
            api.getReceivedMindRecords(receiverId = receiverId).requireData()
        }

    override suspend fun getReceivedAfterNotes(receiverId: Long): Result<ReceivedAfternoteListResponseDto> =
        runCatching {
            api.getReceivedAfterNotes(receiverId = receiverId).requireData()
        }
}
