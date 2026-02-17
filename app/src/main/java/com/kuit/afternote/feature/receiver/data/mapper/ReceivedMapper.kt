package com.kuit.afternote.feature.receiver.data.mapper

import com.kuit.afternote.feature.receiver.data.dto.ReceivedAfternoteResponseDto
import com.kuit.afternote.feature.receiver.data.dto.ReceivedMindRecordResponseDto
import com.kuit.afternote.feature.receiver.data.dto.ReceivedTimeLetterMediaResponseDto
import com.kuit.afternote.feature.receiver.data.dto.ReceivedTimeLetterResponseDto
import com.kuit.afternote.feature.receiver.domain.entity.ReceivedAfternote
import com.kuit.afternote.feature.receiver.domain.entity.ReceivedMindRecord
import com.kuit.afternote.feature.receiver.domain.entity.ReceivedTimeLetter
import com.kuit.afternote.feature.receiver.domain.entity.ReceivedTimeLetterMedia

/**
 * 수신자(Received) API DTO를 도메인 엔티티로 변환하는 Mapper.
 */
object ReceivedMapper {

    /**
     * [ReceivedTimeLetterMediaResponseDto]를 [ReceivedTimeLetterMedia]로 변환합니다.
     */
    fun toReceivedTimeLetterMedia(dto: ReceivedTimeLetterMediaResponseDto): ReceivedTimeLetterMedia =
        ReceivedTimeLetterMedia(
            id = dto.id,
            mediaType = dto.mediaType,
            mediaUrl = dto.mediaUrl
        )

    /**
     * [ReceivedTimeLetterResponseDto]를 [ReceivedTimeLetter]로 변환합니다.
     */
    fun toReceivedTimeLetter(dto: ReceivedTimeLetterResponseDto): ReceivedTimeLetter =
        ReceivedTimeLetter(
            timeLetterId = dto.id,
            timeLetterReceiverId = dto.timeLetterReceiverId,
            title = dto.title,
            content = dto.content,
            sendAt = dto.sendAt,
            status = dto.status,
            senderName = dto.senderName,
            deliveredAt = dto.deliveredAt,
            createdAt = dto.createdAt,
            mediaList = dto.mediaList.map(::toReceivedTimeLetterMedia),
            isRead = dto.isRead
        )

    /**
     * [ReceivedMindRecordResponseDto]를 [ReceivedMindRecord]로 변환합니다.
     */
    fun toReceivedMindRecord(dto: ReceivedMindRecordResponseDto): ReceivedMindRecord =
        ReceivedMindRecord(
            mindRecordId = dto.mindRecordId,
            sourceType = dto.sourceType,
            content = dto.content,
            recordDate = dto.recordDate
        )

    /**
     * [ReceivedAfternoteResponseDto]를 [ReceivedAfternote]로 변환합니다.
     */
    fun toReceivedAfternote(dto: ReceivedAfternoteResponseDto): ReceivedAfternote =
        ReceivedAfternote(
            sourceType = dto.sourceType,
            lastUpdatedAt = dto.lastUpdatedAt
        )
}
