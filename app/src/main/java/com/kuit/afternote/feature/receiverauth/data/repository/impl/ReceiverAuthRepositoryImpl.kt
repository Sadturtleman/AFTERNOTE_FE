package com.kuit.afternote.feature.receiverauth.data.repository.impl

import com.kuit.afternote.data.remote.requireData
import com.kuit.afternote.feature.receiverauth.data.api.ReceiverAuthApiService
import com.kuit.afternote.feature.receiverauth.data.dto.ReceiverAuthVerifyRequestDto
import com.kuit.afternote.feature.receiverauth.data.dto.ReceiverAuthVerifyResponseDto
import com.kuit.afternote.feature.receiverauth.data.dto.ReceivedAfternoteListAuthResponseDto
import com.kuit.afternote.feature.receiverauth.data.dto.ReceivedMindRecordListAuthResponseDto
import com.kuit.afternote.feature.receiverauth.data.dto.ReceivedTimeLetterListAuthResponseDto
import com.kuit.afternote.feature.receiverauth.data.repository.iface.ReceiverAuthRepository
import com.kuit.afternote.feature.receiverauth.domain.entity.ReceiverAuthVerifyResult
import com.kuit.afternote.feature.receiverauth.domain.repository.iface.ReceiverAuthRepository as DomainReceiverAuthRepository
import javax.inject.Inject

/**
 * 수신자 인증 Domain/Data Repository 구현체.
 *
 * - [DomainReceiverAuthRepository]: 인증번호 검증(verify)
 * - [ReceiverAuthRepository]: 타임레터/마인드레코드/애프터노트 목록 조회
 */
class ReceiverAuthRepositoryImpl
    @Inject
    constructor(
        private val api: ReceiverAuthApiService
    ) : DomainReceiverAuthRepository, ReceiverAuthRepository {

    override suspend fun verify(authCode: String): Result<ReceiverAuthVerifyResult> =
        runCatching {
            val dto = api.verify(
                ReceiverAuthVerifyRequestDto(authCode = authCode)
            ).requireData()
            dto.toDomain()
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

private fun ReceiverAuthVerifyResponseDto.toDomain(): ReceiverAuthVerifyResult =
    ReceiverAuthVerifyResult(
        receiverId = receiverId,
        receiverName = receiverName,
        senderName = senderName,
        relation = relation
    )
