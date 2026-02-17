package com.kuit.afternote.feature.receiver.data.repository.iface


/**
 * 수신자(Received) API용 Repository 인터페이스. (스웨거 기준)
 *
 * - 타임레터/마인드레코드 수신자 등록
 */
interface ReceivedRepository {

    /**
     * 타임레터에 수신자를 등록합니다.
     *
     * @param timeLetterId 타임레터 ID
     * @param receiverIds 수신자 ID 목록
     * @param deliveredAt 배달 예정 시간 (optional)
     * @return 등록된 수신자 ID 목록
     */
    suspend fun registerTimeLetterReceivers(
        timeLetterId: Long,
        receiverIds: List<Long>,
        deliveredAt: String? = null
    ): Result<List<Long>>

    /**
     * 마인드레코드에 수신자를 등록합니다.
     *
     * @param mindRecordId 마인드레코드 ID
     * @param receiverIds 수신자 ID 목록
     * @return 등록된 수신자 ID 목록
     */
    suspend fun registerMindRecordReceivers(
        mindRecordId: Long,
        receiverIds: List<Long>
    ): Result<List<Long>>
}
