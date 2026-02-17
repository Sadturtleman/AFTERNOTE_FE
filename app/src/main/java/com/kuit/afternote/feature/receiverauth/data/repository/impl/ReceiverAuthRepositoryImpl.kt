package com.kuit.afternote.feature.receiverauth.data.repository.impl

import com.kuit.afternote.data.remote.requireData
import com.kuit.afternote.feature.receiverauth.data.api.ReceiverAuthApiService
import com.kuit.afternote.feature.receiverauth.data.dto.DeliveryVerificationRequestDto
import com.kuit.afternote.feature.receiverauth.data.dto.DeliveryVerificationResponseDto
import com.kuit.afternote.feature.receiverauth.data.dto.ReceiverAuthVerifyRequestDto
import com.kuit.afternote.feature.receiverauth.data.dto.ReceiverAuthVerifyResponseDto
import com.kuit.afternote.feature.receiverauth.data.dto.ReceivedAfternoteDetailAuthResponseDto
import com.kuit.afternote.feature.receiverauth.data.dto.ReceivedAfternoteListAuthResponseDto
import com.kuit.afternote.feature.receiverauth.data.dto.ReceiverMessageResponseDto
import com.kuit.afternote.feature.receiverauth.data.dto.ReceivedMindRecordDetailAuthResponseDto
import com.kuit.afternote.feature.receiverauth.data.dto.ReceivedMindRecordListAuthResponseDto
import com.kuit.afternote.feature.receiverauth.data.dto.ReceivedTimeLetterDetailAuthResponseDto
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

    override suspend fun submitDeliveryVerification(
        authCode: String,
        request: DeliveryVerificationRequestDto
    ): Result<DeliveryVerificationResponseDto> =
        runCatching {
            api.submitDeliveryVerification(authCode = authCode, body = request).requireData()
        }

    override suspend fun getDeliveryVerificationStatus(
        authCode: String
    ): Result<DeliveryVerificationResponseDto> =
        runCatching {
            api.getDeliveryVerificationStatus(authCode = authCode).requireData()
        }

    override suspend fun getMessage(authCode: String): Result<ReceiverMessageResponseDto> =
        runCatching {
            api.getMessage(authCode = authCode).requireData()
        }

    override suspend fun getTimeLetters(authCode: String): Result<ReceivedTimeLetterListAuthResponseDto> =
        runCatching {
            api.getTimeLetters(authCode = authCode).requireData()
        }

    override suspend fun getTimeLetterDetail(
        authCode: String,
        timeLetterReceiverId: Long
    ): Result<ReceivedTimeLetterDetailAuthResponseDto> =
        runCatching {
            api.getTimeLetterDetail(authCode = authCode, timeLetterReceiverId = timeLetterReceiverId)
                .requireData()
        }

    override suspend fun getMindRecords(authCode: String): Result<ReceivedMindRecordListAuthResponseDto> =
        runCatching {
            api.getMindRecords(authCode = authCode).requireData()
        }

    override suspend fun getMindRecordDetail(
        authCode: String,
        mindRecordId: Long
    ): Result<ReceivedMindRecordDetailAuthResponseDto> =
        runCatching {
            api.getMindRecordDetail(authCode = authCode, mindRecordId = mindRecordId).requireData()
        }

    override suspend fun getAfterNotes(authCode: String): Result<ReceivedAfternoteListAuthResponseDto> =
        runCatching {
            api.getAfterNotes(authCode = authCode).requireData()
        }

    override suspend fun getAfternoteDetail(
        authCode: String,
        afternoteId: Long
    ): Result<ReceivedAfternoteDetailAuthResponseDto> =
        runCatching {
            api.getAfternoteDetail(authCode = authCode, afternoteId = afternoteId).requireData()
        }
}

private fun ReceiverAuthVerifyResponseDto.toDomain(): ReceiverAuthVerifyResult =
    ReceiverAuthVerifyResult(
        receiverId = receiverId,
        receiverName = receiverName,
        senderName = senderName,
        relation = relation
    )
