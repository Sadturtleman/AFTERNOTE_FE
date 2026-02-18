package com.kuit.afternote.feature.receiver.domain.repository.iface

/**
 * 인증번호로 특정 마인드레코드 상세의 대표 이미지 URL을 조회하는 Domain Repository.
 *
 * GET /api/receiver-auth/mind-records/{mindRecordId} (X-Auth-Code) API를 사용합니다.
 */
interface GetMindRecordDetailByAuthCodeRepository {

    /**
     * 마인드레코드 상세의 이미지 목록에서 첫 번째 이미지 URL을 반환합니다.
     * 이미지가 없으면 null을 반환합니다.
     *
     * @param authCode 수신자 인증번호 (X-Auth-Code 헤더)
     * @param mindRecordId 마인드레코드 ID
     */
    suspend fun getMindRecordDetailImageUrl(
        authCode: String,
        mindRecordId: Long
    ): Result<String?>
}
