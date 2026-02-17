package com.kuit.afternote.feature.receiver.data.repository.impl

import com.kuit.afternote.feature.receiver.domain.entity.ReceivedListWithCount
import com.kuit.afternote.feature.receiver.domain.entity.ReceivedTimeLetter
import com.kuit.afternote.feature.receiver.domain.repository.iface.ReceivedTimeLetterRepository
import javax.inject.Inject

/**
 * [ReceivedTimeLetterRepository] 스텁 구현.
 *
 * receiverId 기반 수신 목록 조회 API가 제거되어, 마스터키(인증번호) 기반 조회만 가능합니다.
 * 설정 플로우 등 기존 진입점 유지를 위해 빈 목록/실패를 반환합니다.
 */
class ReceivedTimeLetterRepositoryStub
    @Inject
    constructor() : ReceivedTimeLetterRepository {

    override suspend fun getReceivedTimeLetters(receiverId: Long): Result<ReceivedListWithCount<ReceivedTimeLetter>> =
        Result.success(ReceivedListWithCount(items = emptyList(), totalCount = 0))

    override suspend fun getReceivedTimeLetterDetail(
        receiverId: Long,
        timeLetterReceiverId: Long
    ): Result<ReceivedTimeLetter> =
        Result.failure(UnsupportedOperationException("수신 타임레터 상세는 인증번호(마스터키) 플로우에서만 조회 가능합니다."))
}
