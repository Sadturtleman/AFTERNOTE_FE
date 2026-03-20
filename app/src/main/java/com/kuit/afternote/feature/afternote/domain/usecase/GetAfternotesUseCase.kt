package com.kuit.afternote.feature.afternote.domain.usecase

import com.kuit.afternote.feature.afternote.domain.model.GetAfternotesInput
import com.kuit.afternote.feature.afternote.domain.model.PagedAfternotes
import com.kuit.afternote.feature.afternote.domain.repository.AfternoteRepository
import javax.inject.Inject

/**
 * 애프터노트 목록 조회 UseCase.
 *
 * GET /afternotes?category=&page=&size=
 */
class GetAfternotesUseCase
    @Inject
    constructor(
        private val repository: AfternoteRepository,
    ) {
        suspend operator fun invoke(input: GetAfternotesInput): Result<PagedAfternotes> = repository.getAfternotes(input = input)
    }
