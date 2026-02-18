package com.kuit.afternote.feature.receiverauth.data.repository.impl

import com.kuit.afternote.feature.receiver.domain.entity.ReceivedTimeLetter
import com.kuit.afternote.feature.receiver.domain.repository.iface.GetTimeLetterDetailByAuthCodeRepository
import com.kuit.afternote.feature.receiverauth.data.mapper.toReceivedTimeLetter
import com.kuit.afternote.feature.receiverauth.data.repository.iface.ReceiverAuthRepository
import javax.inject.Inject

/**
 * 인증번호 기반 타임레터 상세 조회 Repository 구현.
 *
 * GET /api/receiver-auth/time-letters/{timeLetterReceiverId} (X-Auth-Code) API를 사용합니다.
 */
class ReceiverAuthTimeLetterDetailRepositoryImpl
    @Inject
    constructor(
        private val receiverAuthRepository: ReceiverAuthRepository
    ) : GetTimeLetterDetailByAuthCodeRepository {

    override suspend fun getTimeLetterDetail(
        authCode: String,
        timeLetterReceiverId: Long
    ): Result<ReceivedTimeLetter> =
        receiverAuthRepository.getTimeLetterDetail(
            authCode = authCode,
            timeLetterReceiverId = timeLetterReceiverId
        ).map { it.toReceivedTimeLetter() }
}
