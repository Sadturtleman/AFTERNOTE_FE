package com.kuit.afternote.feature.timeletter.domain.usecase

import com.kuit.afternote.feature.timeletter.domain.model.TimeLetter
import com.kuit.afternote.feature.timeletter.domain.model.TimeLetterMediaType
import com.kuit.afternote.feature.timeletter.domain.model.TimeLetterStatus
import com.kuit.afternote.feature.timeletter.domain.repository.TimeLetterRepository
import javax.inject.Inject

/**
 * 타임레터 수정 UseCase.
 *
 * @param timeLetterId 수정할 타임레터 ID
 * @param title 제목 (null이면 미변경)
 * @param content 내용 (null이면 미변경)
 * @param sendAt 발송 시각 (null이면 미변경)
 * @param status 상태 (null이면 미변경)
 * @param mediaList 미디어 목록 (null이면 미변경)
 * @return [Result] of [TimeLetter]
 */
class UpdateTimeLetterUseCase
    @Inject
    constructor(
        private val timeLetterRepository: TimeLetterRepository
    ) {
        suspend operator fun invoke(
            timeLetterId: Long,
            title: String?,
            content: String?,
            sendAt: String?,
            status: TimeLetterStatus?,
            mediaList: List<Pair<TimeLetterMediaType, String>>?
        ): Result<TimeLetter> =
            timeLetterRepository.updateTimeLetter(
                timeLetterId,
                title,
                content,
                sendAt,
                status,
                mediaList
            )
    }
