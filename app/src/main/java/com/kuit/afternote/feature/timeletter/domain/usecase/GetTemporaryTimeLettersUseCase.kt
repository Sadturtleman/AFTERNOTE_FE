package com.kuit.afternote.feature.timeletter.domain.usecase

import com.kuit.afternote.feature.timeletter.domain.model.TimeLetterList
import com.kuit.afternote.feature.timeletter.domain.repository.TimeLetterRepository
import javax.inject.Inject

/**
 * 임시저장 타임레터 전체 조회 UseCase. (GET /time-letters/temporary)
 *
 * @return [Result] of [TimeLetterList]
 */
class GetTemporaryTimeLettersUseCase
    @Inject
    constructor(
        private val timeLetterRepository: TimeLetterRepository
    ) {
        suspend operator fun invoke(): Result<TimeLetterList> {
            return timeLetterRepository.getTemporaryTimeLetters()
        }
    }
