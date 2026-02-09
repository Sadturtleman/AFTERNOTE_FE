package com.kuit.afternote.feature.receiver.data.api

import com.kuit.afternote.data.remote.ApiResponse
import com.kuit.afternote.feature.receiver.data.dto.CreateMindRecordReceiverRequestDto
import com.kuit.afternote.feature.receiver.data.dto.CreateTimeLetterReceiverRequestDto
import com.kuit.afternote.feature.receiver.data.dto.ReceivedAfternoteListResponseDto
import com.kuit.afternote.feature.receiver.data.dto.ReceivedMindRecordListResponseDto
import com.kuit.afternote.feature.receiver.data.dto.ReceivedTimeLetterListResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * 수신자(Received) API 서비스. (스웨거 기준)
 *
 * - POST /api/received/time-letters: 타임레터 수신자 등록
 * - POST /api/received/mind-records: 마인드레코드 수신자 등록
 * - GET /api/received/{receiverId}/time-letters: 수신한 타임레터 목록 조회
 * - GET /api/received/{receiverId}/mind-records: 수신한 마인드레코드 목록 조회
 * - GET /api/received/{receiverId}/after-notes: 수신한 애프터노트 목록 조회
 */
interface ReceivedApiService {

    /**
     * 타임레터에 수신자를 등록합니다. 여러 수신자를 한 번에 등록할 수 있습니다.
     *
     * @return data: 등록된 수신자 ID 목록
     */
    @POST("api/received/time-letters")
    suspend fun registerTimeLetterReceivers(
        @Body body: CreateTimeLetterReceiverRequestDto
    ): ApiResponse<List<Long>?>

    /**
     * 마인드레코드에 수신자를 등록합니다. 여러 수신자를 한 번에 등록할 수 있습니다.
     *
     * @return data: 등록된 수신자 ID 목록
     */
    @POST("api/received/mind-records")
    suspend fun registerMindRecordReceivers(
        @Body body: CreateMindRecordReceiverRequestDto
    ): ApiResponse<List<Long>?>

    /**
     * 수신자에게 배달된 타임레터 목록을 조회합니다.
     *
     * @param receiverId 수신자 ID (path)
     * @return data: timeLetters, totalCount
     */
    @GET("api/received/{receiverId}/time-letters")
    suspend fun getReceivedTimeLetters(
        @Path("receiverId") receiverId: Long
    ): ApiResponse<ReceivedTimeLetterListResponseDto?>

    /**
     * 수신자에게 공유된 마인드레코드(일기, 질문답변, 깊은생각) 목록을 조회합니다.
     *
     * @param receiverId 수신자 ID (path)
     * @return data: mindRecords, totalCount
     */
    @GET("api/received/{receiverId}/mind-records")
    suspend fun getReceivedMindRecords(
        @Path("receiverId") receiverId: Long
    ): ApiResponse<ReceivedMindRecordListResponseDto?>

    /**
     * 수신자에게 전달된 애프터노트 목록을 조회합니다.
     *
     * @param receiverId 수신자 ID (path)
     * @return data: afternotes, totalCount
     */
    @GET("api/received/{receiverId}/after-notes")
    suspend fun getReceivedAfterNotes(
        @Path("receiverId") receiverId: Long
    ): ApiResponse<ReceivedAfternoteListResponseDto?>
}
