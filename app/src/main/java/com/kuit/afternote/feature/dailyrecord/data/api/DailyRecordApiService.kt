package com.kuit.afternote.feature.dailyrecord.data.api

import com.kuit.afternote.feature.dailyrecord.data.dto.MindRecordResponse
import com.kuit.afternote.feature.dailyrecord.data.dto.PostRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * 마음의 기록 API 서비스 (스웨거 기준)
 *
 */
interface DailyRecordApiService {
    @GET("mind-records")
    suspend fun getMindRecords(): List<MindRecordResponse>

    @POST("posts")
    suspend fun createPost(@Body postRequestDto: PostRequestDto): Response<Unit>
}
