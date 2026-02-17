package com.kuit.afternote.feature.dailyrecord.data.api

import com.kuit.afternote.feature.dailyrecord.data.dto.DailyQuestionResponse
import com.kuit.afternote.feature.dailyrecord.data.dto.CreateMindRecordRequest
import com.kuit.afternote.feature.dailyrecord.data.dto.MindRecordDetailResponse
import com.kuit.afternote.feature.dailyrecord.data.dto.MindRecordListResponse
import com.kuit.afternote.feature.dailyrecord.data.dto.PostMindRecordRequest
import com.kuit.afternote.feature.dailyrecord.data.dto.PostMindRecordResponse
import com.kuit.afternote.feature.dailyrecord.data.dto.ReceiverEnabledRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 마음의 기록 API 서비스 (Swagger 기준)
 *
 *
 */
interface DailyRecordApiService {

    /**
     * 마음의 기록 목록 조회 (GET /mind-records)
     *
     * @param type 기록 유형 (DAILY_QUESTION, DIARY, DEEP_THOUGHT)
     * @param view 조회 화면 타입 (LIST, CALENDAR)
     * @param year 연도 (view=CALENDAR일 때)
     * @param month 월 1~12 (view=CALENDAR일 때)
     */
    /**
     * 오늘의 데일리 질문 조회 (GET /daily-question)
     * DAILY_QUESTION 타입 기록 작성 시 questionId가 필수이므로, 등록 전에 호출합니다.
     */
    @GET("daily-question")
    suspend fun getDailyQuestion(): DailyQuestionResponse

    @GET("mind-records")
    suspend fun getMindRecords(
        @Query("type") type: String,
        @Query("view") view: String = "LIST",
        @Query("year") year: Int? = null,
        @Query("month") month: Int? = null
    ): MindRecordListResponse

    // 기록 작성
    @POST("mind-records")
    suspend fun createMindRecord(
        @Body request: CreateMindRecordRequest
    ): PostMindRecordResponse


    // 단건 조회
    @GET("mind-records/{recordId}")
    suspend fun getMindRecord(
        @Path("recordId") recordId: Long
    ): Response<MindRecordDetailResponse>

    // 기록 수정
    @PATCH("mind-records/{recordId}")
    suspend fun editMindRecord(
        @Path("recordId") recordId: Long,
        @Body request: PostMindRecordRequest
    ): Response<MindRecordDetailResponse>

    // 기록 삭제
    @DELETE("mind-records/{recordId}")
    suspend fun deleteMindRecord(
        @Path("recordId") recordId: Long
    ): Response<Unit>

    @PATCH("mind-records/{recordId}/receivers/{receiverId}")
    suspend fun setMindRecordReceiverEnabled(
        @Path("recordId") recordId: Long,
        @Path("receiverId") receiverId: Long,
        @Body request: ReceiverEnabledRequest
    ): Response<Unit>

}
