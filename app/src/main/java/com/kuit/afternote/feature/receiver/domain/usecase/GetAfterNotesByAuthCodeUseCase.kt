package com.kuit.afternote.feature.receiver.domain.usecase

import com.kuit.afternote.feature.receiver.domain.entity.ReceivedListWithCount
import com.kuit.afternote.feature.receiver.domain.entity.ReceivedAfternote
import com.kuit.afternote.feature.receiver.domain.repository.iface.GetAfterNotesByAuthCodeRepository
import javax.inject.Inject

/**
 * 수신자 인증번호(authCode)로 전달된 애프터노트 목록·전체 개수 조회 UseCase.
 *
 * GET /api/receiver-auth/after-notes (X-Auth-Code) API를 사용합니다.
 */
class GetAfterNotesByAuthCodeUseCase
    @Inject
    constructor(
        private val getAfterNotesByAuthCodeRepository: GetAfterNotesByAuthCodeRepository
    ) {
        suspend operator fun invoke(authCode: String): Result<ReceivedListWithCount<ReceivedAfternote>> =
            getAfterNotesByAuthCodeRepository.getAfterNotes(authCode)
}
