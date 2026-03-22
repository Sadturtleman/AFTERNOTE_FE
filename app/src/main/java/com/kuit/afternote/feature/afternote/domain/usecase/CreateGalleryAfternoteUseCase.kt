package com.kuit.afternote.feature.afternote.domain.usecase

import com.kuit.afternote.feature.afternote.domain.model.CreateGalleryInput
import com.kuit.afternote.feature.afternote.domain.repository.AfternoteRepository
import javax.inject.Inject

/**
 * 갤러리 및 파일 애프터노트 생성 UseCase.
 *
 * POST /api/afternotes (category = GALLERY)
 */
class CreateGalleryAfternoteUseCase
    @Inject
    constructor(
        private val repository: AfternoteRepository,
    ) {
        suspend operator fun invoke(input: CreateGalleryInput): Result<Long> =
            repository.createGallery(
                input = input,
            )
    }
