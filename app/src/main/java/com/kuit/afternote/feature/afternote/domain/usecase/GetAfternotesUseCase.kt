package com.kuit.afternote.feature.afternote.domain.usecase

import com.kuit.afternote.feature.afternote.domain.model.PagedAfternotes
import com.kuit.afternote.feature.afternote.domain.repository.iface.AfternoteRepository
import javax.inject.Inject

/**
 * 애프터노트 목록 조회 UseCase.
 *
 * GET /afternotes?category=&page=&size=
 */
class GetAfternotesUseCase
    @Inject
    constructor(
        private val repository: AfternoteRepository
    ) {
        suspend operator fun invoke(
            category: String? = null,
            page: Int = 0,
            size: Int = 10
        ): Result<PagedAfternotes> = repository.getAfternotes(category = category, page = page, size = size)
    }

