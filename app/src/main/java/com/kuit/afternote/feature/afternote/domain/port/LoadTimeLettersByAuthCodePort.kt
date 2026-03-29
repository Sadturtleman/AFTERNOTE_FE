package com.kuit.afternote.feature.afternote.domain.port

import com.kuit.afternote.feature.afternote.domain.model.received.InboxTimeLetter
import com.kuit.afternote.feature.afternote.domain.model.received.ReceivedListWithCount

fun interface LoadTimeLettersByAuthCodePort {
    suspend operator fun invoke(authCode: String): Result<ReceivedListWithCount<InboxTimeLetter>>
}
