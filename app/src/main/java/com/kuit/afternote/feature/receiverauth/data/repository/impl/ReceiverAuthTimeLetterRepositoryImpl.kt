package com.kuit.afternote.feature.receiverauth.data.repository.impl

import com.kuit.afternote.feature.receiver.domain.entity.ReceivedListWithCount
import com.kuit.afternote.feature.receiver.domain.entity.ReceivedTimeLetter
import com.kuit.afternote.feature.receiver.domain.repository.iface.GetTimeLettersByAuthCodeRepository
import com.kuit.afternote.feature.receiverauth.data.mapper.toReceivedTimeLetter
import com.kuit.afternote.feature.receiverauth.data.repository.iface.ReceiverAuthRepository
import javax.inject.Inject

/**
 * 인증번호 기반 타임레터 목록 조회 Repository 구현.
 *
 * receiver-auth API 응답을 receiver 도메인 엔티티로 매핑하여 반환합니다.
 */
class ReceiverAuthTimeLetterRepositoryImpl
    @Inject
    constructor(
        private val receiverAuthRepository: ReceiverAuthRepository
    ) : GetTimeLettersByAuthCodeRepository {

    override suspend fun getTimeLetters(authCode: String): Result<ReceivedListWithCount<ReceivedTimeLetter>> =
        receiverAuthRepository.getTimeLetters(authCode).map { dto ->
            ReceivedListWithCount(
                items = dto.timeLetters.map { it.toReceivedTimeLetter() },
                totalCount = dto.totalCount
            )
        }
}
