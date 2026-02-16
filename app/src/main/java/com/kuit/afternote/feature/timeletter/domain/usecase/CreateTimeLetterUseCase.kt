package com.kuit.afternote.feature.timeletter.domain.usecase

import com.kuit.afternote.feature.timeletter.domain.model.TimeLetter
import com.kuit.afternote.feature.timeletter.domain.model.TimeLetterMediaType
import com.kuit.afternote.feature.timeletter.domain.model.TimeLetterStatus
import com.kuit.afternote.feature.timeletter.domain.repository.TimeLetterRepository
import javax.inject.Inject

/**
 * 타임레터 생성 UseCase. (임시저장 DRAFT / 정식등록 SCHEDULED)
 *
 * @param title 제목
 * @param content 내용
 * @param sendAt 발송 시각 (ISO 등)
 * @param status 상태 (DRAFT / SCHEDULED)
 * @param mediaList 미디어 목록 (타입, URL 쌍)
 * @param receiverIds 수신자 ID 목록
 * @param deliveredAt 배달 예정 시간 (미지정 시 서버에서 sendAt 사용)
 * @return [Result] of [TimeLetter]
 */
class CreateTimeLetterUseCase
    @Inject
    constructor(
        private val timeLetterRepository: TimeLetterRepository
    ) {
        suspend operator fun invoke(
            title: String?,
            content: String?,
            sendAt: String?,
            status: TimeLetterStatus,
            mediaList: List<Pair<TimeLetterMediaType, String>>?,
            receiverIds: List<Long>,
            deliveredAt: String?
        ): Result<TimeLetter> =
            timeLetterRepository.createTimeLetter(
                title,
                content,
                sendAt,
                status,
                mediaList,
                receiverIds,
                deliveredAt
            )
    }
