package com.kuit.afternote.feature.receiverauth.data.api

import com.kuit.afternote.data.remote.ApiResponse
import com.kuit.afternote.feature.receiverauth.data.dto.DeliveryVerificationRequestDto
import com.kuit.afternote.feature.receiverauth.data.dto.DeliveryVerificationResponseDto
import com.kuit.afternote.feature.receiverauth.data.dto.ReceiverAuthPresignedUrlRequestDto
import com.kuit.afternote.feature.receiverauth.data.dto.ReceiverAuthPresignedUrlResponseDto
import com.kuit.afternote.feature.receiverauth.data.dto.ReceiverAuthVerifyRequestDto
import com.kuit.afternote.feature.receiverauth.data.dto.ReceiverAuthVerifyResponseDto
import com.kuit.afternote.feature.receiverauth.data.dto.ReceiverMessageResponseDto
import com.kuit.afternote.feature.receiverauth.data.dto.ReceivedAfternoteDetailAuthResponseDto
import com.kuit.afternote.feature.receiverauth.data.dto.ReceivedAfternoteListAuthResponseDto
import com.kuit.afternote.feature.receiverauth.data.dto.ReceivedMindRecordDetailAuthResponseDto
import com.kuit.afternote.feature.receiverauth.data.dto.ReceivedMindRecordListAuthResponseDto
import com.kuit.afternote.feature.receiverauth.data.dto.ReceivedTimeLetterDetailAuthResponseDto
import com.kuit.afternote.feature.receiverauth.data.dto.ReceivedTimeLetterListAuthResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.POST

/**
 * 수신자 인증번호 기반 콘텐츠 조회 API 서비스. (스웨거 기준)
 *
 * - POST /api/receiver-auth/verify: 인증번호 검증
 * - POST /api/receiver-auth/presigned-url: 수신자 파일 업로드용 Presigned URL 생성
 * - POST /api/receiver-auth/delivery-verification: 사망확인 서류 제출
 * - GET /api/receiver-auth/delivery-verification/status: 사망확인 인증 상태 조회
 * - GET /api/receiver-auth/message: 발신자 메시지 조회
 * - GET /api/receiver-auth/time-letters: 타임레터 목록 조회
 * - GET /api/receiver-auth/time-letters/{timeLetterReceiverId}: 타임레터 상세 조회
 * - GET /api/receiver-auth/mind-records: 마인드레코드 목록 조회
 * - GET /api/receiver-auth/mind-records/{mindRecordId}: 마인드레코드 상세 조회
 * - GET /api/receiver-auth/after-notes: 애프터노트 목록 조회
 * - GET /api/receiver-auth/after-notes/{afternoteId}: 애프터노트 상세 조회
 */
interface ReceiverAuthApiService {

    /**
     * 수신자 인증번호를 검증하고 수신자/발신자 정보를 반환합니다.
     *
     * @param body authCode (UUID)
     * @return receiverId, receiverName, senderName, relation
     */
    @POST("api/receiver-auth/verify")
    suspend fun verify(
        @Body body: ReceiverAuthVerifyRequestDto
    ): ApiResponse<ReceiverAuthVerifyResponseDto?>

    /**
     * 수신자 파일 업로드용 Presigned URL을 생성합니다.
     *
     * 수신자가 사망확인 서류(PDF, 이미지)를 S3에 업로드하기 위한 Presigned URL을 생성합니다.
     *
     * @param authCode 수신자 인증번호 (UUID, X-Auth-Code 헤더)
     * @param body extension (예: pdf, jpg, png)
     * @return presignedUrl, fileUrl, contentType
     */
    @POST("api/receiver-auth/presigned-url")
    suspend fun getPresignedUrl(
        @Header("X-Auth-Code") authCode: String,
        @Body body: ReceiverAuthPresignedUrlRequestDto
    ): ApiResponse<ReceiverAuthPresignedUrlResponseDto?>

    /**
     * 사망확인 서류(사망진단서, 가족관계증명서)를 제출합니다.
     *
     * 전달 조건이 DEATH_CERTIFICATE인 경우 수신자가 인증 서류를 제출합니다.
     *
     * @param authCode 수신자 인증번호 (UUID, X-Auth-Code 헤더)
     * @param body deathCertificateUrl, familyRelationCertificateUrl
     */
    @POST("api/receiver-auth/delivery-verification")
    suspend fun submitDeliveryVerification(
        @Header("X-Auth-Code") authCode: String,
        @Body body: DeliveryVerificationRequestDto
    ): ApiResponse<DeliveryVerificationResponseDto?>

    /**
     * 수신자가 마지막으로 제출한 인증 요청 상태를 조회합니다.
     *
     * @param authCode 수신자 인증번호 (UUID, X-Auth-Code 헤더)
     */
    @GET("api/receiver-auth/delivery-verification/status")
    suspend fun getDeliveryVerificationStatus(
        @Header("X-Auth-Code") authCode: String
    ): ApiResponse<DeliveryVerificationResponseDto?>

    /**
     * 인증번호를 통해 발신자가 남긴 메시지를 조회합니다.
     *
     * @param authCode 수신자 인증번호 (UUID, X-Auth-Code 헤더)
     */
    @GET("api/receiver-auth/message")
    suspend fun getMessage(
        @Header("X-Auth-Code") authCode: String
    ): ApiResponse<ReceiverMessageResponseDto?>

    /**
     * 인증번호를 통해 수신자에게 배달된 타임레터 목록을 조회합니다.
     *
     * @param authCode 수신자 인증번호 (UUID, X-Auth-Code 헤더)
     */
    @GET("api/receiver-auth/time-letters")
    suspend fun getTimeLetters(
        @Header("X-Auth-Code") authCode: String
    ): ApiResponse<ReceivedTimeLetterListAuthResponseDto?>

    /**
     * 인증번호를 통해 수신한 특정 타임레터를 상세 조회합니다. 읽음 처리도 함께 수행됩니다.
     *
     * @param authCode 수신자 인증번호 (UUID, X-Auth-Code 헤더)
     * @param timeLetterReceiverId 수신 타임레터 ID
     */
    @GET("api/receiver-auth/time-letters/{timeLetterReceiverId}")
    suspend fun getTimeLetterDetail(
        @Header("X-Auth-Code") authCode: String,
        @Path("timeLetterReceiverId") timeLetterReceiverId: Long
    ): ApiResponse<ReceivedTimeLetterDetailAuthResponseDto?>

    /**
     * 인증번호를 통해 수신자에게 공유된 마인드레코드 목록을 조회합니다.
     *
     * @param authCode 수신자 인증번호 (UUID, X-Auth-Code 헤더)
     */
    @GET("api/receiver-auth/mind-records")
    suspend fun getMindRecords(
        @Header("X-Auth-Code") authCode: String
    ): ApiResponse<ReceivedMindRecordListAuthResponseDto?>

    /**
     * 인증번호를 통해 수신한 특정 마인드레코드의 상세 내용을 조회합니다.
     *
     * @param authCode 수신자 인증번호 (UUID, X-Auth-Code 헤더)
     * @param mindRecordId 마인드레코드 ID
     */
    @GET("api/receiver-auth/mind-records/{mindRecordId}")
    suspend fun getMindRecordDetail(
        @Header("X-Auth-Code") authCode: String,
        @Path("mindRecordId") mindRecordId: Long
    ): ApiResponse<ReceivedMindRecordDetailAuthResponseDto?>

    /**
     * 인증번호를 통해 수신자에게 전달된 애프터노트 목록을 조회합니다.
     *
     * @param authCode 수신자 인증번호 (UUID, X-Auth-Code 헤더)
     */
    @GET("api/receiver-auth/after-notes")
    suspend fun getAfterNotes(
        @Header("X-Auth-Code") authCode: String
    ): ApiResponse<ReceivedAfternoteListAuthResponseDto?>

    /**
     * 인증번호를 통해 수신한 특정 애프터노트의 상세 내용을 조회합니다.
     *
     * @param authCode 수신자 인증번호 (UUID, X-Auth-Code 헤더)
     * @param afternoteId 애프터노트 ID
     */
    @GET("api/receiver-auth/after-notes/{afternoteId}")
    suspend fun getAfternoteDetail(
        @Header("X-Auth-Code") authCode: String,
        @Path("afternoteId") afternoteId: Long
    ): ApiResponse<ReceivedAfternoteDetailAuthResponseDto?>
}
