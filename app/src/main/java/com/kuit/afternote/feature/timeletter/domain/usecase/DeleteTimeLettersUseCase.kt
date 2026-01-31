package com.kuit.afternote.feature.timeletter.domain.usecase

import com.kuit.afternote.feature.timeletter.domain.repository.TimeLetterRepository
import javax.inject.Inject

/**
 * 타임레터 단일/다건 삭제 UseCase.
 *
 * @param timeLetterIds 삭제할 타임레터 ID 목록
 * @return [Result] of [Unit]
 */
class DeleteTimeLettersUseCase
    @Inject
    constructor(
        private val timeLetterRepository: TimeLetterRepository
    ) {
        suspend operator fun invoke(timeLetterIds: List<Long>): Result<Unit> {
            return timeLetterRepository.deleteTimeLetters(timeLetterIds)
        }
    }
