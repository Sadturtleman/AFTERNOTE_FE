package com.kuit.afternote.feature.receiver.data.repository.iface

import com.kuit.afternote.feature.receiver.data.dto.ReceivedAfternoteListResponseDto
import com.kuit.afternote.feature.receiver.data.dto.ReceivedMindRecordListResponseDto
import com.kuit.afternote.feature.receiver.data.dto.ReceivedTimeLetterListResponseDto
import com.kuit.afternote.feature.receiver.data.dto.ReceivedTimeLetterResponseDto

/**
 * 수신자(Received) API용 Repository 인터페이스. (스웨거 기준)
 *
 * - 타임레터/마인드레코드 수신자 등록
 * - 수신한 타임레터/마인드레코드/애프터노트 목록 조회
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

    /**
     * 수신자에게 배달된 타임레터 목록을 조회합니다.
     *
     * @param receiverId 수신자 ID
     * @return timeLetters, totalCount
     */
    suspend fun getReceivedTimeLetters(receiverId: Long): Result<ReceivedTimeLetterListResponseDto>

    /**
     * 수신한 타임레터 상세를 조회하고 읽음 처리합니다.
     *
     * @param receiverId 수신자 ID
     * @param timeLetterReceiverId 상세 조회용 수신 타임레터 ID
     * @return 타임레터 상세 DTO
     */
    suspend fun getReceivedTimeLetterDetail(
        receiverId: Long,
        timeLetterReceiverId: Long
    ): Result<ReceivedTimeLetterResponseDto>

    /**
     * 수신자에게 공유된 마인드레코드 목록을 조회합니다.
     *
     * @param receiverId 수신자 ID
     * @return mindRecords, totalCount
     */
    suspend fun getReceivedMindRecords(receiverId: Long): Result<ReceivedMindRecordListResponseDto>

    /**
     * 수신자에게 전달된 애프터노트 목록을 조회합니다.
     *
     * @param receiverId 수신자 ID
     * @return afternotes, totalCount
     */
    suspend fun getReceivedAfterNotes(receiverId: Long): Result<ReceivedAfternoteListResponseDto>
}
