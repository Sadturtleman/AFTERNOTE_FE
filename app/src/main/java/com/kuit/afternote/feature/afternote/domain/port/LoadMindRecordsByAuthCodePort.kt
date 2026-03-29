package com.kuit.afternote.feature.afternote.domain.port

import com.kuit.afternote.feature.afternote.domain.model.received.InboxMindRecord
import com.kuit.afternote.feature.afternote.domain.model.received.ReceivedListWithCount

fun interface LoadMindRecordsByAuthCodePort {
    suspend operator fun invoke(authCode: String): Result<ReceivedListWithCount<InboxMindRecord>>
}
