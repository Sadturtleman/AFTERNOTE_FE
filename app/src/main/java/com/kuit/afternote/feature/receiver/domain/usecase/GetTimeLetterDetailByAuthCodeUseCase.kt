package com.kuit.afternote.feature.receiver.domain.usecase

import com.kuit.afternote.feature.receiver.domain.entity.ReceivedTimeLetter
import com.kuit.afternote.feature.receiver.domain.repository.iface.GetTimeLetterDetailByAuthCodeRepository
import javax.inject.Inject

/**
 * 수신자 인증번호(authCode)와 수신 타임레터 ID로 타임레터 상세를 조회하는 UseCase.
 *
 * GET /api/receiver-auth/time-letters/{timeLetterReceiverId} (X-Auth-Code) API를 사용합니다.
 */
class GetTimeLetterDetailByAuthCodeUseCase
    @Inject
    constructor(
        private val getTimeLetterDetailByAuthCodeRepository: GetTimeLetterDetailByAuthCodeRepository
    ) {
        suspend operator fun invoke(
            authCode: String,
            timeLetterReceiverId: Long
        ): Result<ReceivedTimeLetter> =
            getTimeLetterDetailByAuthCodeRepository.getTimeLetterDetail(
                authCode = authCode,
                timeLetterReceiverId = timeLetterReceiverId
            )
    }
