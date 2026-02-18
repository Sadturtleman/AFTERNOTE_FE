package com.kuit.afternote.feature.receiverauth.data.repository.iface

import com.kuit.afternote.feature.receiverauth.data.dto.ReceivedAfternoteListAuthResponseDto
import com.kuit.afternote.feature.receiverauth.data.dto.ReceivedMindRecordListAuthResponseDto
import com.kuit.afternote.feature.receiverauth.data.dto.ReceivedTimeLetterListAuthResponseDto

/**
 * 수신자 인증번호 기반 목록 조회용 Data Repository 인터페이스.
 *
 * 인증번호 검증(verify)은 Domain [com.kuit.afternote.feature.receiverauth.domain.repository.iface.ReceiverAuthRepository]를 사용합니다.
 */
interface ReceiverAuthRepository {

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
