package com.kuit.afternote.feature.receiverauth.data.repository.iface

import com.kuit.afternote.feature.receiverauth.data.dto.DeliveryVerificationRequestDto
import com.kuit.afternote.feature.receiverauth.data.dto.DeliveryVerificationResponseDto
import com.kuit.afternote.feature.receiverauth.data.dto.ReceiverAuthPresignedUrlResponseDto
import com.kuit.afternote.feature.receiverauth.data.dto.ReceiverMessageResponseDto
import com.kuit.afternote.feature.receiverauth.data.dto.ReceivedAfternoteDetailAuthResponseDto
import com.kuit.afternote.feature.receiverauth.data.dto.ReceivedAfternoteListAuthResponseDto
import com.kuit.afternote.feature.receiverauth.data.dto.ReceivedMindRecordDetailAuthResponseDto
import com.kuit.afternote.feature.receiverauth.data.dto.ReceivedMindRecordListAuthResponseDto
import com.kuit.afternote.feature.receiverauth.data.dto.ReceivedTimeLetterDetailAuthResponseDto
import com.kuit.afternote.feature.receiverauth.data.dto.ReceivedTimeLetterListAuthResponseDto

/**
 * 수신자 인증번호 기반 목록 조회용 Data Repository 인터페이스.
 *
 * 인증번호 검증(verify)은 Domain [com.kuit.afternote.feature.receiverauth.domain.repository.iface.ReceiverAuthRepository]를 사용합니다.
 */
interface ReceiverAuthRepository {

    /**
     * 수신자 파일 업로드용 Presigned URL을 생성합니다.
     *
     * 수신자가 사망확인 서류(PDF, 이미지)를 S3에 업로드하기 위한 Presigned URL을 생성합니다.
     *
     * @param authCode 수신자 인증번호 (UUID)
     * @param extension 파일 확장자 (예: pdf, jpg, png)
     * @return presignedUrl, fileUrl, contentType
     */
    suspend fun getPresignedUrl(
        authCode: String,
        extension: String
    ): Result<ReceiverAuthPresignedUrlResponseDto>

    /**
     * 사망확인 서류(사망진단서, 가족관계증명서)를 제출합니다.
     *
     * 전달 조건이 DEATH_CERTIFICATE인 경우 사용합니다.
     *
     * @param authCode 수신자 인증번호 (UUID)
     * @param request deathCertificateUrl, familyRelationCertificateUrl
     * @return 제출 결과 (인증 요청 ID, 상태 등)
     */
    suspend fun submitDeliveryVerification(
        authCode: String,
        request: DeliveryVerificationRequestDto
    ): Result<DeliveryVerificationResponseDto>

    /**
     * 수신자가 마지막으로 제출한 인증 요청 상태를 조회합니다.
     *
     * @param authCode 수신자 인증번호 (UUID)
     */
    suspend fun getDeliveryVerificationStatus(authCode: String): Result<DeliveryVerificationResponseDto>

    /**
     * 인증번호를 통해 발신자가 남긴 메시지를 조회합니다.
     *
     * @param authCode 수신자 인증번호 (UUID)
     */
    suspend fun getMessage(authCode: String): Result<ReceiverMessageResponseDto>

    /**
     * 인증번호를 통해 수신자에게 배달된 타임레터 목록을 조회합니다.
     *
     * @param authCode 수신자 인증번호 (UUID)
     */
    suspend fun getTimeLetters(authCode: String): Result<ReceivedTimeLetterListAuthResponseDto>

    /**
     * 인증번호를 통해 수신한 특정 타임레터를 상세 조회합니다. 읽음 처리도 함께 수행됩니다.
     *
     * @param authCode 수신자 인증번호 (UUID)
     * @param timeLetterReceiverId 수신 타임레터 ID
     */
    suspend fun getTimeLetterDetail(
        authCode: String,
        timeLetterReceiverId: Long
    ): Result<ReceivedTimeLetterDetailAuthResponseDto>

    /**
     * 인증번호를 통해 수신자에게 공유된 마인드레코드 목록을 조회합니다.
     *
     * @param authCode 수신자 인증번호 (UUID)
     */
    suspend fun getMindRecords(authCode: String): Result<ReceivedMindRecordListAuthResponseDto>

    /**
     * 인증번호를 통해 수신한 특정 마인드레코드의 상세 내용을 조회합니다.
     *
     * @param authCode 수신자 인증번호 (UUID)
     * @param mindRecordId 마인드레코드 ID
     */
    suspend fun getMindRecordDetail(
        authCode: String,
        mindRecordId: Long
    ): Result<ReceivedMindRecordDetailAuthResponseDto>

    /**
     * 인증번호를 통해 수신자에게 전달된 애프터노트 목록을 조회합니다.
     *
     * @param authCode 수신자 인증번호 (UUID)
     */
    suspend fun getAfterNotes(authCode: String): Result<ReceivedAfternoteListAuthResponseDto>

    /**
     * 인증번호를 통해 수신한 특정 애프터노트의 상세 내용을 조회합니다.
     *
     * @param authCode 수신자 인증번호 (UUID)
     * @param afternoteId 애프터노트 ID
     */
    suspend fun getAfternoteDetail(
        authCode: String,
        afternoteId: Long
    ): Result<ReceivedAfternoteDetailAuthResponseDto>
}
