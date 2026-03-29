package com.kuit.afternote.feature.afternote.domain.model

import com.kuit.afternote.feature.afternote.domain.model.received.InboxMindRecord
import com.kuit.afternote.feature.afternote.domain.model.received.InboxTimeLetter
import com.kuit.afternote.feature.afternote.domain.model.received.ReceivedAfternote

/**
 * 수신자에게 전달된 타임레터·마인드레코드·애프터노트 전체 조회 결과.
 *
 * DownloadAllReceivedUseCase의 반환 타입으로 사용됩니다.
 */
data class DownloadAllResult(
    val timeLetters: List<InboxTimeLetter>,
    val mindRecords: List<InboxMindRecord>,
    val afternotes: List<ReceivedAfternote>,
)
