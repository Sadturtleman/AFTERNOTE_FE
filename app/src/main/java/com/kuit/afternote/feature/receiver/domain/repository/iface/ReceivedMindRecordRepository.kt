package com.kuit.afternote.feature.receiver.domain.repository.iface

import com.kuit.afternote.feature.receiver.domain.entity.ReceivedMindRecord

/**
 * 수신자에게 공유된 마인드레코드 목록을 조회하는 도메인 Repository 인터페이스.
 *
 * GET /api/received/{receiverId}/mind-records API에 대응합니다.
 */
interface ReceivedMindRecordRepository {

    /**
     * 수신자에게 공유된 마인드레코드 목록을 조회합니다.
     *
     * @param receiverId 수신자 ID
     * @return 성공 시 [ReceivedMindRecord] 목록, 실패 시 [Result.failure]
     */
    suspend fun getReceivedMindRecords(receiverId: Long): Result<List<ReceivedMindRecord>>
}
