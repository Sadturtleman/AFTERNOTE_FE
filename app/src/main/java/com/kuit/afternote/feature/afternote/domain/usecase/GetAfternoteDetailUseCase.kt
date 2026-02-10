package com.kuit.afternote.feature.afternote.domain.usecase

import com.kuit.afternote.feature.afternote.domain.model.AfternoteDetail
import com.kuit.afternote.feature.afternote.domain.repository.iface.AfternoteRepository
import javax.inject.Inject

/**
 * 애프터노트 상세 조회 UseCase.
 *
 * GET /api/afternotes/{afternoteId}
 */
class GetAfternoteDetailUseCase
    @Inject
    constructor(
        private val repository: AfternoteRepository
    ) {
        suspend operator fun invoke(afternoteId: Long): Result<AfternoteDetail> =
            repository.getAfternoteDetail(afternoteId = afternoteId)
    }
