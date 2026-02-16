package com.kuit.afternote.feature.receiverauth.data.repository.impl

import com.kuit.afternote.data.remote.requireData
import com.kuit.afternote.feature.receiverauth.data.api.ReceiverAuthApiService
import com.kuit.afternote.feature.receiverauth.data.dto.ReceiverAuthVerifyRequestDto
import com.kuit.afternote.feature.receiverauth.data.dto.ReceiverAuthVerifyResponseDto
import com.kuit.afternote.feature.receiverauth.data.dto.ReceivedAfternoteListAuthResponseDto
import com.kuit.afternote.feature.receiverauth.data.dto.ReceivedMindRecordListAuthResponseDto
import com.kuit.afternote.feature.receiverauth.data.dto.ReceivedTimeLetterListAuthResponseDto
import com.kuit.afternote.feature.receiverauth.data.repository.iface.ReceiverAuthRepository
import javax.inject.Inject

/**
 * [ReceiverAuthRepository] 구현체. (스웨거 기준)
 *
 * ReceiverAuthApiService를 통해 인증번호 검증 및 수신 콘텐츠 조회를 수행합니다.
 */
class ReceiverAuthRepositoryImpl
    @Inject
    constructor(
        private val api: ReceiverAuthApiService
    ) : ReceiverAuthRepository {

    override suspend fun verify(authCode: String): Result<ReceiverAuthVerifyResponseDto> =
        runCatching {
            api.verify(
                ReceiverAuthVerifyRequestDto(authCode = authCode)
            ).requireData()
        }

    override suspend fun getTimeLetters(authCode: String): Result<ReceivedTimeLetterListAuthResponseDto> =
        runCatching {
            api.getTimeLetters(authCode = authCode).requireData()
        }

    override suspend fun getMindRecords(authCode: String): Result<ReceivedMindRecordListAuthResponseDto> =
        runCatching {
            api.getMindRecords(authCode = authCode).requireData()
        }

    override suspend fun getAfterNotes(authCode: String): Result<ReceivedAfternoteListAuthResponseDto> =
        runCatching {
            api.getAfterNotes(authCode = authCode).requireData()
        }
}
