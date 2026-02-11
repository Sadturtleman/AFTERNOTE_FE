package com.kuit.afternote.feature.receiver.data.mapper

import com.kuit.afternote.feature.receiver.data.dto.ReceivedAfternoteResponseDto
import com.kuit.afternote.feature.receiver.data.dto.ReceivedTimeLetterResponseDto
import com.kuit.afternote.feature.receiver.domain.entity.ReceivedAfternote
import com.kuit.afternote.feature.receiver.domain.entity.ReceivedTimeLetter

/**
 * 수신자(Received) API DTO를 도메인 엔티티로 변환하는 Mapper.
 */
object ReceivedMapper {

    /**
     * [ReceivedTimeLetterResponseDto]를 [ReceivedTimeLetter]로 변환합니다.
     */
    fun toReceivedTimeLetter(dto: ReceivedTimeLetterResponseDto): ReceivedTimeLetter =
        ReceivedTimeLetter(
            timeLetterId = dto.timeLetterId,
            receiverName = dto.receiverName,
            sendAt = dto.sendAt,
            title = dto.title,
            content = dto.content
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
