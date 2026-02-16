package com.kuit.afternote.feature.receiver.domain.usecase

import com.kuit.afternote.feature.receiver.domain.entity.ReceivedListWithCount
import com.kuit.afternote.feature.receiver.domain.entity.ReceivedMindRecord
import com.kuit.afternote.feature.receiver.domain.repository.iface.GetMindRecordsByAuthCodeRepository
import javax.inject.Inject

/**
 * 수신자 인증번호(authCode)로 공유된 마인드레코드 목록·전체 개수 조회 UseCase.
 *
 * GET /api/receiver-auth/mind-records (X-Auth-Code) API를 사용합니다.
 */
class GetMindRecordsByAuthCodeUseCase
    @Inject
    constructor(
        private val getMindRecordsByAuthCodeRepository: GetMindRecordsByAuthCodeRepository
    ) {
        suspend operator fun invoke(authCode: String): Result<ReceivedListWithCount<ReceivedMindRecord>> =
            getMindRecordsByAuthCodeRepository.getMindRecords(authCode)
}
