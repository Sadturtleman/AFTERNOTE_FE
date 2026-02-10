package com.kuit.afternote.feature.afternote.domain.usecase

import com.kuit.afternote.feature.afternote.domain.repository.iface.AfternoteRepository
import javax.inject.Inject

/**
 * 애프터노트 삭제 UseCase.
 *
 * DELETE /api/afternotes/{afternoteId}
 */
class DeleteAfternoteUseCase
    @Inject
    constructor(
        private val repository: AfternoteRepository
    ) {
        suspend operator fun invoke(afternoteId: Long): Result<Unit> =
            repository.deleteAfternote(afternoteId = afternoteId)
    }
