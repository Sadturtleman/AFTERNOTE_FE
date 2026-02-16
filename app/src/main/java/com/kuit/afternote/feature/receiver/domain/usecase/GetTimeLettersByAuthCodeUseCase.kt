package com.kuit.afternote.feature.receiver.domain.usecase

import com.kuit.afternote.feature.receiver.domain.entity.ReceivedListWithCount
import com.kuit.afternote.feature.receiver.domain.entity.ReceivedTimeLetter
import com.kuit.afternote.feature.receiver.domain.repository.iface.GetTimeLettersByAuthCodeRepository
import javax.inject.Inject

/**
 * 수신자 인증번호(authCode)로 배달된 타임레터 목록·전체 개수 조회 UseCase.
 *
 * GET /api/receiver-auth/time-letters (X-Auth-Code) API를 사용합니다.
 */
class GetTimeLettersByAuthCodeUseCase
    @Inject
    constructor(
        private val getTimeLettersByAuthCodeRepository: GetTimeLettersByAuthCodeRepository
    ) {
        suspend operator fun invoke(authCode: String): Result<ReceivedListWithCount<ReceivedTimeLetter>> =
            getTimeLettersByAuthCodeRepository.getTimeLetters(authCode)
}
