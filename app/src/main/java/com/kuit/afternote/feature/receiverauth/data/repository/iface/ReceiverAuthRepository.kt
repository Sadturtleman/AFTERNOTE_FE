package com.kuit.afternote.feature.receiverauth.data.repository.iface

import com.kuit.afternote.feature.receiverauth.data.dto.ReceiverAuthVerifyResponseDto
import com.kuit.afternote.feature.receiverauth.data.dto.ReceivedAfternoteListAuthResponseDto
import com.kuit.afternote.feature.receiverauth.data.dto.ReceivedMindRecordListAuthResponseDto
import com.kuit.afternote.feature.receiverauth.data.dto.ReceivedTimeLetterListAuthResponseDto

/**
 * 수신자 인증번호 기반 콘텐츠 조회용 Repository 인터페이스. (스웨거 기준)
 *
 * - 인증번호 검증
 * - 타임레터/마인드레코드/애프터노트 목록 조회
 */
interface ReceiverAuthRepository {

    /**
     * 수신자 인증번호를 검증하고 수신자/발신자 정보를 반환합니다.
     *
     * @param authCode 수신자 인증번호 (UUID)
     * @return receiverId, receiverName, senderName, relation
     */
    suspend fun verify(authCode: String): Result<ReceiverAuthVerifyResponseDto>

    /**
     * 인증번호를 통해 수신자에게 배달된 타임레터 목록을 조회합니다.
     *
     * @param authCode 수신자 인증번호 (UUID)
     */
    suspend fun getTimeLetters(authCode: String): Result<ReceivedTimeLetterListAuthResponseDto>

    /**
     * 인증번호를 통해 수신자에게 공유된 마인드레코드 목록을 조회합니다.
     *
     * @param authCode 수신자 인증번호 (UUID)
     */
    suspend fun getMindRecords(authCode: String): Result<ReceivedMindRecordListAuthResponseDto>

    /**
     * 인증번호를 통해 수신자에게 전달된 애프터노트 목록을 조회합니다.
     *
     * @param authCode 수신자 인증번호 (UUID)
     */
    suspend fun getAfterNotes(authCode: String): Result<ReceivedAfternoteListAuthResponseDto>
}
