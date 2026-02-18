package com.kuit.afternote.feature.receiver.domain.usecase

import com.kuit.afternote.feature.receiver.domain.repository.iface.GetMindRecordDetailByAuthCodeRepository
import javax.inject.Inject

/**
 * 수신자 인증번호(authCode)와 마인드레코드 ID로 상세의 대표 이미지 URL을 조회하는 UseCase.
 *
 * GET /api/receiver-auth/mind-records/{mindRecordId} (X-Auth-Code) API를 사용합니다.
 */
class GetMindRecordDetailByAuthCodeUseCase
    @Inject
    constructor(
        private val getMindRecordDetailByAuthCodeRepository: GetMindRecordDetailByAuthCodeRepository
    ) {

    /**
     * 마인드레코드 상세의 첫 번째 이미지 URL을 반환합니다. 없으면 null.
     *
     * @param authCode 수신자 인증번호
     * @param mindRecordId 마인드레코드 ID
     */
    suspend operator fun invoke(
        authCode: String,
        mindRecordId: Long
    ): Result<String?> =
        getMindRecordDetailByAuthCodeRepository.getMindRecordDetailImageUrl(
            authCode = authCode,
            mindRecordId = mindRecordId
        )
}
