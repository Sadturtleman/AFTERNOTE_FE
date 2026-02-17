package com.kuit.afternote.feature.receiverauth.data.mapper

import com.kuit.afternote.feature.receiver.domain.entity.ReceivedAfternote
import com.kuit.afternote.feature.receiver.domain.entity.ReceivedMindRecord
import com.kuit.afternote.feature.receiver.domain.entity.ReceivedTimeLetter
import com.kuit.afternote.feature.receiverauth.data.dto.ReceivedAfternoteAuthResponseDto
import com.kuit.afternote.feature.receiverauth.data.dto.ReceivedMindRecordAuthResponseDto
import com.kuit.afternote.feature.receiverauth.data.dto.ReceivedTimeLetterAuthResponseDto

/**
 * receiver-auth API DTO를 receiver 도메인 엔티티로 매핑합니다.
 */

fun ReceivedTimeLetterAuthResponseDto.toReceivedTimeLetter(): ReceivedTimeLetter =
    ReceivedTimeLetter(
        timeLetterId = id,
        timeLetterReceiverId = id,
        title = title,
        content = content,
        sendAt = sendAt,
        status = status,
        senderName = senderName,
        deliveredAt = deliveredAt,
        createdAt = createdAt,
        mediaList = emptyList(),
        isRead = false
    )

fun ReceivedMindRecordAuthResponseDto.toReceivedMindRecord(): ReceivedMindRecord =
    ReceivedMindRecord(
        mindRecordId = id,
        sourceType = type,
        content = title,
        recordDate = recordDate
    )

fun ReceivedAfternoteAuthResponseDto.toReceivedAfternote(): ReceivedAfternote =
    ReceivedAfternote(
        sourceType = category.orEmpty(),
        lastUpdatedAt = createdAt.orEmpty(),
        leaveMessage = leaveMessage
    )
