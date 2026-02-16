package com.kuit.afternote.feature.receiverauth.data.repository.impl

import com.kuit.afternote.feature.receiver.domain.entity.ReceivedListWithCount
import com.kuit.afternote.feature.receiver.domain.entity.ReceivedAfternote
import com.kuit.afternote.feature.receiver.domain.repository.iface.GetAfterNotesByAuthCodeRepository
import com.kuit.afternote.feature.receiverauth.data.mapper.toReceivedAfternote
import com.kuit.afternote.feature.receiverauth.data.repository.iface.ReceiverAuthRepository
import javax.inject.Inject

/**
 * 인증번호 기반 애프터노트 목록 조회 Repository 구현.
 *
 * receiver-auth API 응답을 receiver 도메인 엔티티로 매핑하여 반환합니다.
 */
class ReceiverAuthAfternoteRepositoryImpl
    @Inject
    constructor(
        private val receiverAuthRepository: ReceiverAuthRepository
    ) : GetAfterNotesByAuthCodeRepository {

    override suspend fun getAfterNotes(authCode: String): Result<ReceivedListWithCount<ReceivedAfternote>> =
        receiverAuthRepository.getAfterNotes(authCode).map { dto ->
            ReceivedListWithCount(
                items = dto.afternotes.map { it.toReceivedAfternote() },
                totalCount = dto.totalCount
            )
        }
}
