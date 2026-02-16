package com.kuit.afternote.feature.receiver.domain.repository.iface

import com.kuit.afternote.feature.receiver.domain.entity.ReceivedListWithCount
import com.kuit.afternote.feature.receiver.domain.entity.ReceivedTimeLetter

/**
 * 수신자에게 배달된 타임레터 목록/상세를 조회하는 도메인 Repository 인터페이스.
 *
 * GET /api/received/{receiverId}/time-letters, time-letters/{timeLetterReceiverId} API에 대응합니다.
 */
interface ReceivedTimeLetterRepository {

    /**
     * 수신자에게 배달된 타임레터 목록과 전체 개수를 조회합니다.
     *
     * @param receiverId 수신자 ID
     * @return 성공 시 [ReceivedListWithCount], 실패 시 [Result.failure]
     */
    suspend fun getReceivedTimeLetters(receiverId: Long): Result<ReceivedListWithCount<ReceivedTimeLetter>>

    /**
     * 수신한 타임레터 상세를 조회하고 읽음 처리합니다.
     *
     * @param receiverId 수신자 ID
     * @param timeLetterReceiverId 상세 조회용 수신 타임레터 ID
     * @return 성공 시 [ReceivedTimeLetter], 실패 시 [Result.failure]
     */
    suspend fun getReceivedTimeLetterDetail(
        receiverId: Long,
        timeLetterReceiverId: Long
    ): Result<ReceivedTimeLetter>
}
