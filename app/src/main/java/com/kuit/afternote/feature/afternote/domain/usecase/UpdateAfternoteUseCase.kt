package com.kuit.afternote.feature.afternote.domain.usecase

import com.kuit.afternote.feature.afternote.domain.model.UpdateRequestInput
import com.kuit.afternote.feature.afternote.domain.repository.AfternoteRepository
import javax.inject.Inject

/**
 * 애프터노트 수정 UseCase.
 *
 * PATCH /api/afternotes/{afternoteId}
 */
class UpdateAfternoteUseCase
    @Inject
    constructor(
        private val repository: AfternoteRepository,
    ) {
        suspend operator fun invoke(
            afternoteId: Long,
            body: UpdateRequestInput,
        ): Result<Long> =
            repository.updateAfternote(
                afternoteId = afternoteId,
                input = body,
            )
    }
