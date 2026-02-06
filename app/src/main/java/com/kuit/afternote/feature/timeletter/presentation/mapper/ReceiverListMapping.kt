package com.kuit.afternote.feature.timeletter.presentation.mapper

import com.kuit.afternote.feature.timeletter.data.dto.TimeLetterReceiver
import com.kuit.afternote.feature.user.domain.model.ReceiverListItem

/**
 * GET /users/receivers 응답(ReceiverListItem)을 타임레터 수신자 목록 UI용(TimeLetterReceiver)으로 변환합니다.
 * 설정 > 수신자 목록과 타임레터 수신자 목록/드롭다운에서 동일한 소스를 사용할 때 공통으로 사용합니다.
 */
internal fun List<ReceiverListItem>.toTimeLetterReceivers(): List<TimeLetterReceiver> =
    map { item ->
        TimeLetterReceiver(
            id = item.receiverId,
            receiver_name = item.name,
            send_at = "",
            title = "",
            content = "",
            image_url = null,
            relation = item.relation
        )
    }
