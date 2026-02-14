package com.kuit.afternote.feature.afternote.domain.usecase

import com.kuit.afternote.feature.afternote.data.dto.AfternoteUpdateRequestDto
import com.kuit.afternote.feature.afternote.domain.repository.iface.AfternoteRepository
import javax.inject.Inject

/**
 * 애프터노트 수정 UseCase.
 *
 * PATCH /api/afternotes/{afternoteId}
 */
class UpdateAfternoteUseCase
    @Inject
    constructor(
        private val repository: AfternoteRepository
    ) {
        suspend operator fun invoke(
            afternoteId: Long,
            body: AfternoteUpdateRequestDto
        ): Result<Long> = repository.updateAfternote(
            afternoteId = afternoteId,
            body = body
        )
    }
