package com.kuit.afternote.feature.receiver.domain.repository.iface

import com.kuit.afternote.feature.receiver.domain.entity.ReceivedListWithCount
import com.kuit.afternote.feature.receiver.domain.entity.ReceivedAfternote

/**
 * 수신자 인증번호(authCode)로 애프터노트 목록을 조회하는 Domain Repository.
 *
 * GET /api/receiver-auth/after-notes (X-Auth-Code) API를 사용합니다.
 */
interface GetAfterNotesByAuthCodeRepository {

    /**
     * 인증번호로 전달된 애프터노트 목록과 전체 개수를 조회합니다.
     *
     * @param authCode 수신자 인증번호 (마스터키, X-Auth-Code 헤더)
     */
    suspend fun getAfterNotes(authCode: String): Result<ReceivedListWithCount<ReceivedAfternote>>
}
