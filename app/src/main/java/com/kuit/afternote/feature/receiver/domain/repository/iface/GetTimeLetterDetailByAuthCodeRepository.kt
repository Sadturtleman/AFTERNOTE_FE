package com.kuit.afternote.feature.receiver.domain.repository.iface

import com.kuit.afternote.feature.receiver.domain.entity.ReceivedTimeLetter

/**
 * 수신자 인증번호(authCode)와 수신 타임레터 ID로 타임레터 상세를 조회하는 Domain Repository.
 *
 * GET /api/receiver-auth/time-letters/{timeLetterReceiverId} (X-Auth-Code) API를 사용합니다.
 */
interface GetTimeLetterDetailByAuthCodeRepository {

    /**
     * 인증번호로 특정 타임레터 상세를 조회합니다. 읽음 처리도 함께 수행됩니다.
     *
     * @param authCode 수신자 인증번호 (X-Auth-Code 헤더)
     * @param timeLetterReceiverId 수신 타임레터 ID
     */
    suspend fun getTimeLetterDetail(
        authCode: String,
        timeLetterReceiverId: Long
    ): Result<ReceivedTimeLetter>
}
