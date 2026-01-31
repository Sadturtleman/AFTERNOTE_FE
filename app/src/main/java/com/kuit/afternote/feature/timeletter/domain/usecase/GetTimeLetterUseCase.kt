package com.kuit.afternote.feature.timeletter.domain.usecase

import com.kuit.afternote.feature.timeletter.domain.model.TimeLetter
import com.kuit.afternote.feature.timeletter.domain.repository.TimeLetterRepository
import javax.inject.Inject

/**
 * 타임레터 단건 조회 UseCase.
 *
 * @param timeLetterId 조회할 타임레터 ID
 * @return [Result] of [TimeLetter]
 */
class GetTimeLetterUseCase
    @Inject
    constructor(
        private val timeLetterRepository: TimeLetterRepository
    ) {
        suspend operator fun invoke(timeLetterId: Long): Result<TimeLetter> =
            timeLetterRepository.getTimeLetter(timeLetterId)
    }
