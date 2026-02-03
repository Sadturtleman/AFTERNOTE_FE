package com.kuit.afternote.feature.timeletter.domain.usecase

import com.kuit.afternote.feature.timeletter.domain.repository.TimeLetterRepository
import javax.inject.Inject

/**
 * 임시저장 타임레터 전체 삭제 UseCase. (DELETE /time-letters/temporary)
 *
 * @return [Result] of [Unit]
 */
class DeleteAllTemporaryUseCase
    @Inject
    constructor(
        private val timeLetterRepository: TimeLetterRepository
    ) {
        suspend operator fun invoke(): Result<Unit> = timeLetterRepository.deleteAllTemporary()
    }
