package com.kuit.afternote.feature.dailyrecord.data.api

import com.kuit.afternote.feature.dailyrecord.data.dto.*
import retrofit2.Response
import retrofit2.http.*

/**
 * 마음의 기록 API 서비스 (Swagger 기준)
 */
interface DailyRecordApiService {

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
