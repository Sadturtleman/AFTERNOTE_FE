package com.kuit.afternote.feature.receiver.domain.repository.iface

import com.kuit.afternote.feature.receiver.domain.entity.ReceivedAfternoteDetail

/**
 * 수신자 인증번호(authCode)와 애프터노트 ID로 애프터노트 상세를 조회하는 Domain Repository.
 *
 * GET /api/receiver-auth/after-notes/{afternoteId} (X-Auth-Code) API를 사용합니다.
 */
interface GetAfternoteDetailByAuthCodeRepository {

    /**
     * 인증번호와 애프터노트 ID로 상세(플레이리스트 등)를 조회합니다.
     *
     * @param authCode 수신자 인증번호 (X-Auth-Code 헤더)
     * @param afternoteId 애프터노트 ID
     */
    suspend fun getAfternoteDetail(
        authCode: String,
        afternoteId: Long
    ): Result<ReceivedAfternoteDetail>
}
