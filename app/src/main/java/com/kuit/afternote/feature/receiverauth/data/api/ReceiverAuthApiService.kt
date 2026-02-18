package com.kuit.afternote.feature.receiverauth.data.api

import com.kuit.afternote.data.remote.ApiResponse
import com.kuit.afternote.feature.receiverauth.data.dto.ReceiverAuthVerifyRequestDto
import com.kuit.afternote.feature.receiverauth.data.dto.ReceiverAuthVerifyResponseDto
import com.kuit.afternote.feature.receiverauth.data.dto.ReceivedAfternoteListAuthResponseDto
import com.kuit.afternote.feature.receiverauth.data.dto.ReceivedMindRecordListAuthResponseDto
import com.kuit.afternote.feature.receiverauth.data.dto.ReceivedTimeLetterListAuthResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * 수신자 인증번호 기반 콘텐츠 조회 API 서비스. (스웨거 기준)
 *
 * - POST /api/receiver-auth/verify: 인증번호 검증
 * - GET /api/receiver-auth/time-letters: 타임레터 목록 조회
 * - GET /api/receiver-auth/mind-records: 마인드레코드 목록 조회
 * - GET /api/receiver-auth/after-notes: 애프터노트 목록 조회
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
     * 인증번호를 통해 수신자에게 배달된 타임레터 목록을 조회합니다.
     *
     * @param authCode 수신자 인증번호 (UUID, X-Auth-Code 헤더)
     */
    @GET("api/receiver-auth/time-letters")
    suspend fun getTimeLetters(
        @Header("X-Auth-Code") authCode: String
    ): ApiResponse<ReceivedTimeLetterListAuthResponseDto?>

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
     * 인증번호를 통해 수신자에게 전달된 애프터노트 목록을 조회합니다.
     *
     * @param authCode 수신자 인증번호 (UUID, X-Auth-Code 헤더)
     */
    @GET("api/receiver-auth/after-notes")
    suspend fun getAfterNotes(
        @Header("X-Auth-Code") authCode: String
    ): ApiResponse<ReceivedAfternoteListAuthResponseDto?>
}
