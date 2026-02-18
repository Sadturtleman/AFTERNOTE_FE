package com.kuit.afternote.feature.receiver.domain.usecase

import com.kuit.afternote.feature.receiver.domain.entity.ReceivedAfternoteDetail
import com.kuit.afternote.feature.receiver.domain.repository.iface.GetAfternoteDetailByAuthCodeRepository
import javax.inject.Inject

/**
 * 수신자 인증번호(authCode)와 애프터노트 ID로 애프터노트 상세를 조회하는 UseCase.
 *
 * GET /api/receiver-auth/after-notes/{afternoteId} (X-Auth-Code) API를 사용합니다.
 */
class GetAfternoteDetailByAuthCodeUseCase
    @Inject
    constructor(
        private val getAfternoteDetailByAuthCodeRepository: GetAfternoteDetailByAuthCodeRepository
    ) {
        suspend operator fun invoke(
            authCode: String,
            afternoteId: Long
        ): Result<ReceivedAfternoteDetail> =
            getAfternoteDetailByAuthCodeRepository.getAfternoteDetail(
                authCode = authCode,
                afternoteId = afternoteId
            )
    }
