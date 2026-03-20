package com.kuit.afternote.feature.afternote.domain.usecase

import com.kuit.afternote.feature.afternote.domain.model.playlist.CreatePlaylistInput
import com.kuit.afternote.feature.afternote.domain.repository.AfternoteRepository
import javax.inject.Inject

/**
 * 추모 가이드라인(플레이리스트) 애프터노트 생성 UseCase.
 *
 * POST /api/afternotes (category = PLAYLIST)
 */
class CreatePlaylistAfternoteUseCase
    @Inject
    constructor(
        private val repository: AfternoteRepository,
    ) {
        suspend operator fun invoke(input: CreatePlaylistInput): Result<Long> =
            repository.createPlaylist(
                input = input,
            )
    }
