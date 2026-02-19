package com.kuit.afternote.feature.receiver.domain.repository.iface

import com.kuit.afternote.feature.receiver.domain.entity.ReceivedMindRecordDetail

/**
 * 인증번호로 특정 마인드레코드 상세를 조회하는 Domain Repository.
 *
 * GET /api/receiver-auth/mind-records/{mindRecordId} (X-Auth-Code) API를 사용합니다.
 */
interface GetMindRecordDetailByAuthCodeRepository {

    /**
     * 마인드레코드 상세를 반환합니다.
     *
     * @param authCode 수신자 인증번호 (X-Auth-Code 헤더)
     * @param mindRecordId 마인드레코드 ID
     */
    suspend fun getMindRecordDetail(
        authCode: String,
        mindRecordId: Long
    ): Result<ReceivedMindRecordDetail>
}
