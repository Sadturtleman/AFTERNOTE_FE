package com.kuit.afternote.feature.receiver.domain.repository.iface

import com.kuit.afternote.feature.receiver.domain.entity.ReceivedAfternote

/**
 * 수신자에게 전달된 애프터노트 목록을 조회하는 도메인 Repository 인터페이스.
 *
 * GET /api/received/{receiverId}/after-notes API에 대응합니다.
 */
interface ReceivedAfternoteRepository {

    /**
     * 수신자에게 전달된 애프터노트 목록을 조회합니다.
     *
     * @param receiverId 수신자 ID
     * @return 성공 시 [ReceivedAfternote] 목록, 실패 시 [Result.failure]
     */
    suspend fun getReceivedAfterNotes(receiverId: Long): Result<List<ReceivedAfternote>>
}
