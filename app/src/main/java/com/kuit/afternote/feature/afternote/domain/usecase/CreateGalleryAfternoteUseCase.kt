package com.kuit.afternote.feature.afternote.domain.usecase

import com.kuit.afternote.feature.afternote.domain.repository.iface.AfternoteRepository
import javax.inject.Inject

/**
 * 갤러리 및 파일 애프터노트 생성 UseCase.
 *
 * POST /api/afternotes (category = GALLERY)
 */
class CreateGalleryAfternoteUseCase
    @Inject
    constructor(
        private val repository: AfternoteRepository
    ) {
        suspend operator fun invoke(
            title: String,
            processMethod: String,
            actions: List<String>,
            leaveMessage: String? = null,
            receiverIds: List<Long> = emptyList()
        ): Result<Long> = repository.createGallery(
            title = title,
            processMethod = processMethod,
            actions = actions,
            leaveMessage = leaveMessage,
            receiverIds = receiverIds
        )
    }
